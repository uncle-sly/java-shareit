package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.ItemUpdateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    public List<ItemDto> getOwnersItems(Long userId) {

        userRepository.getById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));

        return itemMapper.toItemDtoList(itemRepository.getOwnersItems(userId));
    }

    public ItemDto getById(Long userId, Long itemId) {

        userRepository.getById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));

        return itemMapper.toItemDto(itemRepository.getById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(Item.class, " c ID = " + itemId + ", не найдена.")));
    }

    public List<ItemDto> getSearchedItems(Long userId, String text) {

        if (text.isBlank()) {
            return List.of();
        }
        userRepository.getById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));

        return itemMapper.toItemDtoList(itemRepository.getSearchedItems(text.trim().toLowerCase()));
    }

    public ItemDto create(Long userId, ItemDto itemDto) {
        User itemOwner = userRepository.getById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));
        Item newItem = itemMapper.toItem(itemDto);
        newItem.setOwner(itemOwner);

        return itemMapper.toItemDto(itemRepository.create(newItem));
    }

    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {

        User itemOwner = userRepository.getById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));
        Item item = itemRepository.getById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(Item.class, " c ID = " + itemId + ", не найдена."));

        if (!item.getOwner().equals(itemOwner)) {
            throw new ItemUpdateException("Не владелец! Редактировать вешь, может только ее владелец.");
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
        return itemMapper.toItemDto(item);
    }
}
