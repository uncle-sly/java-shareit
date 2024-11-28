package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class InMemoryItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> userItems = new HashMap<>();
    private Long itemId = 0L;

    public List<Item> getOwnersItems(Long userId) {
        return userItems.get(userId);
    }

    public Optional<Item> getById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    public List<Item> getSearchedItems(String text) {
        return items.values().stream()
                .filter(item -> item.getAvailable() &&
                        (item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)))
                .toList();
    }

    public Item create(Item item) {
        item.setId(generateItemId());
        items.put(item.getId(), item);
        userItems.computeIfAbsent(item.getOwner().getId(), id -> new ArrayList<>()).add(item);
        return item;
    }

    private long generateItemId() {
        return ++itemId;
    }
}
