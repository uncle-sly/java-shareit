package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
//import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
//import ru.practicum.shareit.validation.OnCreate;

import java.util.List;

import static ru.practicum.shareit.utility.Constants.USER_ID;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    //POST /requests — добавить новый запрос вещи
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader(USER_ID) long userId, @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.create(userId, itemRequestDto);
    }

    //    GET /requests — получить список своих запросов вместе с данными об ответах на них.
    @GetMapping
    public List<ItemRequestDto> getUserRequestsList(@RequestHeader(USER_ID) long userId) {
        return itemRequestService.getUserRequestsList(userId);
    }


    //    GET /requests/all — получить список запросов, созданных другими пользователями.
    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestsList(@RequestHeader(USER_ID) long userId) {
        return itemRequestService.getAll(userId);
    }

    //    GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об ответах на него
    //    в том же формате, что и в эндпоинте GET /requests.
    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader(USER_ID) long userId, @PathVariable("requestId") long requestId) {
        return itemRequestService.getById(userId, requestId);
    }

}