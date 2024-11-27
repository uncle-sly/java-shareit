package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.validation.OnCreate;
import ru.practicum.shareit.user.validation.OnUpdate;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    //заголовок в константу
    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getOwnersItems(@RequestHeader(USER_ID) long userId) {
        return itemService.getOwnersItems(userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getById(@RequestHeader(USER_ID) long userId, @PathVariable("itemId") long itemId) {
        return itemService.getById(userId, itemId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getSearchedItems(@RequestHeader(USER_ID) long userId, @RequestParam(name = "text") String text) {
        return itemService.getSearchedItems(userId, text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader(USER_ID) long userId, @Validated(OnCreate.class) @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@RequestHeader(USER_ID) long userId, @PathVariable("itemId") long itemId, @Valid @Validated(OnUpdate.class) @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }
}