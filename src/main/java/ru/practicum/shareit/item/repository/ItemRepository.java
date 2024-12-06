package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> getOwnersItems(Long userId);

    Optional<Item> getById(Long itemId);

    List<Item> getSearchedItems(String text);

    Item create(Item item);

}
