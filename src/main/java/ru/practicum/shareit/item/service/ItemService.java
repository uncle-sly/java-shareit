package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnersItemDto;

import java.util.List;

public interface ItemService {

    List<OwnersItemDto> getOwnersItems(Long userId);

    ItemDto getById(Long userId, Long itemId);

    List<ItemDto> getSearchedItems(Long userId, String text);

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    CommentDto createComment(Long userId, Long itemId, CommentDto commentDto);
}
