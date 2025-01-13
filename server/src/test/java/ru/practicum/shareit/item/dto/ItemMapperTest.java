package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    private ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        itemMapper = new ItemMapperImpl();
    }

    @Test
    void toItemAnswerDtoWithItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);

        ItemAnswerDto itemAnswerDto = itemMapper.toItemAnswerDto(item);

        assertNotNull(itemAnswerDto);
        assertEquals(item.getId(), itemAnswerDto.getId());
        assertEquals(item.getName(), itemAnswerDto.getName());
    }

    @Test
    void toItemAnswerDtoWithNullItem() {
        ItemAnswerDto itemAnswerDto = itemMapper.toItemAnswerDto(null);
        assertNull(itemAnswerDto);
    }

    @Test
    void toItemAnswerDtoListWithItems() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");
        item1.setDescription("Description 1");
        item1.setAvailable(true);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");
        item2.setDescription("Description 2");
        item2.setAvailable(false);

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        List<ItemAnswerDto> itemAnswerDtos = itemMapper.toItemAnswerDtoList(items);

        assertNotNull(itemAnswerDtos);
        assertEquals(items.size(), itemAnswerDtos.size());

        ItemAnswerDto dto1 = itemAnswerDtos.getFirst();
        assertEquals(item1.getId(), dto1.getId());
        assertEquals(item1.getName(), dto1.getName());

        ItemAnswerDto dto2 = itemAnswerDtos.getLast();
        assertEquals(item2.getId(), dto2.getId());
        assertEquals(item2.getName(), dto2.getName());
    }

    @Test
    void toItemAnswerDtoListWithEmptyList() {
        List<Item> items = new ArrayList<>();

        List<ItemAnswerDto> itemAnswerDtos = itemMapper.toItemAnswerDtoList(items);

        assertNotNull(itemAnswerDtos);
        assertTrue(itemAnswerDtos.isEmpty());
    }

    @Test
    void toItemAnswerDtoListWithNullList() {
        List<ItemAnswerDto> itemAnswerDtos = itemMapper.toItemAnswerDtoList(null);
        assertNull(itemAnswerDtos);
    }
}
