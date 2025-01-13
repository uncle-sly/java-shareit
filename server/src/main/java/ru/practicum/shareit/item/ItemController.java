package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnersItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.utility.Constants.USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<OwnersItemDto> getOwnersItems(@RequestHeader(USER_ID) long userId) {
        return itemService.getOwnersItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader(USER_ID) long userId, @PathVariable("itemId") long itemId) {
        return itemService.getById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearchedItems(@RequestHeader(USER_ID) long userId, @RequestParam(name = "text") String text) {
        return itemService.getSearchedItems(userId, text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader(USER_ID) long userId, @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID) long userId, @PathVariable("itemId") long itemId, @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(USER_ID) long userId, @PathVariable("itemId") long itemId,
                                     @RequestBody CommentDto commentDto) {
        return itemService.createComment(userId, itemId, commentDto);
    }

}