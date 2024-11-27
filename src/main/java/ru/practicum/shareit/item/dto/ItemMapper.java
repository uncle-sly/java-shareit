package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDto toItemDto(Item item);

    List<ItemDto> toItemDtoList(List<Item> items);

    Item toItem(ItemDto itemDto);


}
