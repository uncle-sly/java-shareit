package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.EntityUpdateException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    public List<OwnersItemDto> getOwnersItems(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));

        List<OwnersItemDto> ownersItemDtos = itemMapper.toOwnersItemDtoList(itemRepository.findByOwner(user));

        ownersItemDtos.forEach(ownersItemDto -> {
            List<Booking> itemBookings = bookingRepository.findAllByItemIdOrderByStartDateDesc(ownersItemDto.getId());
            Booking lastBooking = itemBookings.stream()
                    .filter(booking -> booking.getStart().isBefore(LocalDateTime.now())).findFirst().orElse(null);
            Booking nextBooking = itemBookings.stream()
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now())).findFirst().orElse(null);

            ownersItemDto.setLastBooking(lastBooking != null ? lastBooking.getStart() : null);
            ownersItemDto.setNextBooking(nextBooking != null ? nextBooking.getStart() : null);

            List<Comment> comments = commentRepository.findByItem_Id(ownersItemDto.getId());
            ownersItemDto.setComments(commentMapper.toDtos(comments));
        });
        return ownersItemDtos;
    }

    public ItemDto getById(Long userId, Long itemId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));

        ItemDto itemDto = itemMapper.toItemDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(Item.class, " c ID = " + itemId + ", не найдена.")));

        List<Comment> comments = commentRepository.findByItem_Id(itemId);
        itemDto.setComments(commentMapper.toDtos(comments));
        return itemDto;
    }

    public List<ItemDto> getSearchedItems(Long userId, String text) {

        if (text.isBlank()) {
            return List.of();
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));

        return itemMapper.toItemDtoList(
                itemRepository.findAvailableByNameOrDescriptionContainingText(text.trim().toLowerCase()));
    }

    public ItemDto create(Long userId, ItemDto itemDto) {
        User itemOwner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));
        Item newItem = itemMapper.toItem(itemDto);
        newItem.setOwner(itemOwner);

        return itemMapper.toItemDto(itemRepository.save(newItem));
    }

    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {

        User itemOwner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(Item.class, " c ID = " + itemId + ", не найдена."));

        if (!item.getOwner().equals(itemOwner)) {
            throw new EntityUpdateException(Item.class, "User c ID = " + userId + " - Не владелец! Редактировать вешь, может только ее владелец.");
        }

        final String name = itemDto.getName();
        if (name != null && !name.isBlank()) {
            item.setName(name);
        }
        final String description = itemDto.getDescription();
        if (description != null && !description.isBlank()) {
            item.setDescription(description);
        }
        final Boolean available = itemDto.getAvailable();
        if (available != null) {
            item.setAvailable(available);
        }
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    public CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));

        Booking booking = bookingRepository.findOneByItemIdAndBookerIdAndStatus(itemId, user.getId(), BookingStatus.APPROVED)
                .orElseThrow(() -> new EntityNotFoundException(Booking.class, " не найдено."));

        if (!booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Отказ, бронирование не закончено.");
        }

        Comment newComment = commentMapper.fromDto(commentDto);
        newComment.setItem(booking.getItem());
        newComment.setAuthor(booking.getBooker());
        newComment.setCreated(LocalDateTime.now());

        return commentMapper.toDto(commentRepository.save(newComment));
    }

}
