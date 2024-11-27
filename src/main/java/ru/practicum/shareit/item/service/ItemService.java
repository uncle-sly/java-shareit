package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getOwnersItems(Long userId);

    ItemDto getById(Long userId, Long itemId);

    List<ItemDto> getSearchedItems(Long userId, String text);

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);
}
