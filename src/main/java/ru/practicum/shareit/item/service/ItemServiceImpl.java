package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemUpdateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper = ItemMapper.INSTANCE;

    public List<ItemDto> getOwnersItems(Long userId) {

        userRepository.getById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь c ID - " + userId + ", не найден."));

        return itemMapper.toItemDtoList(itemRepository.getOwnersItems(userId));
    }

    public ItemDto getById(Long userId, Long itemId) {

        userRepository.getById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь c ID - " + userId + ", не найден."));

        return itemMapper.toItemDto(itemRepository.getById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь c ID - " + itemId + ", не найдена.")));
    }

    public List<ItemDto> getSearchedItems(Long userId, String text) {

        userRepository.getById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь c ID - " + userId + ", не найден."));

        String searchText = String.format(text).trim().toLowerCase();

        return itemMapper.toItemDtoList(!(text.isEmpty() || text.isBlank()) ? itemRepository.getSearchedItems(searchText) : List.of());
    }

    public ItemDto create(Long userId, ItemDto itemDto) {
        User itemOwner = userRepository.getById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь c ID - " + userId + ", не найден."));
        Item newItem = itemMapper.toItem(itemDto);
        newItem.setOwner(itemOwner);

        return itemMapper.toItemDto(itemRepository.create(newItem));
    }

    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {

        User itemOwner = userRepository.getById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь c ID - " + userId + ", не найден."));
        Item item = itemRepository.getById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь c ID - " + itemId + ", не найдена."));

        if (!item.getOwner().equals(itemOwner)) {
            throw new ItemUpdateException("Не владелец! Редактировать вешь, может только ее владелец.");
        }

        Item updatedItem = itemMapper.toItem(itemDto);
        updatedItem.setId(item.getId());
        updatedItem.setName(updatedItem.getName() != null ? updatedItem.getName() : item.getName());
        updatedItem.setDescription(updatedItem.getDescription() != null ? updatedItem.getDescription() : item.getDescription());
        updatedItem.setAvailable(updatedItem.getAvailable() != null ? updatedItem.getAvailable() : item.getAvailable());
        updatedItem.setOwner(item.getOwner());

        return itemMapper.toItemDto(itemRepository.update(updatedItem));
    }
}
