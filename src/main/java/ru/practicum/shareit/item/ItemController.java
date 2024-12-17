package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnersItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.OnCreate;
import ru.practicum.shareit.validation.OnUpdate;

import java.util.List;

import static ru.practicum.shareit.utility.Constants.USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
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
    public ItemDto create(@RequestHeader(USER_ID) long userId, @Validated(OnCreate.class) @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID) long userId, @PathVariable("itemId") long itemId, @Valid @Validated(OnUpdate.class) @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(USER_ID) long userId, @PathVariable("itemId") long itemId,
                                    @Validated(OnCreate.class) @RequestBody CommentDto commentDto) {
        return itemService.createComment(userId, itemId, commentDto);
    }

}