package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validation.OnCreate;


import static ru.practicum.shareit.utility.Constants.USER_ID;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    //POST /requests — добавить новый запрос вещи
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader(USER_ID) long userId, @Validated(OnCreate.class) @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.create(userId, itemRequestDto);
    }

    //    GET /requests — получить список своих запросов вместе с данными об ответах на них.
    @GetMapping
    public ResponseEntity<Object> getUserRequestsList(@RequestHeader(USER_ID) long userId) {
        return itemRequestClient.getUserRequestsList(userId);
    }

    //    GET /requests/all — получить список запросов, созданных другими пользователями.
    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsList(@RequestHeader(USER_ID) long userId) {
        return itemRequestClient.getAll(userId);
    }

    //    GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об ответах на него
    //    в том же формате, что и в эндпоинте GET /requests.
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(USER_ID) long userId, @PathVariable("requestId") long requestId) {
        return itemRequestClient.getById(userId, requestId);
    }

}