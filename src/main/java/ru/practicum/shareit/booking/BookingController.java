package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validation.OnCreate;

import java.util.List;

import static ru.practicum.shareit.utility.Constants.USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    //После создания запрос находится в статусе WAITING — «ожидает подтверждения».
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader(USER_ID) long userId, @Validated(OnCreate.class) @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    //    PATCH /bookings/{bookingId}?approved={approved}
    //    Может быть выполнено только владельцем вещи. Затем статус бронирования становится либо APPROVED, либо REJECTED.
    @PatchMapping("/{bookingId}")
    public BookingDto update(@RequestHeader(USER_ID) long userId,
                             @PathVariable("bookingId") long bookingId, @RequestParam("approved") boolean approved) {

        return bookingService.update(userId, bookingId, approved);
    }

    // GET /bookings/{bookingId}
    // Получение данных о конкретном бронировании (включая его статус). Может быть выполнено либо автором бронирования,
    // либо владельцем вещи, к которой относится бронирование.
    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(USER_ID) long userId, @PathVariable("bookingId") long bookingId) {

        return bookingService.getById(userId, bookingId);
    }

    // GET /bookings?state={state}
    // Получение списка всех бронирований текущего пользователя.
    @GetMapping
    public List<BookingDto> getCurrentUserBookings(@RequestHeader(USER_ID) long userId, @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {

        BookingState state = BookingState.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Некорректный state: " + stateParam);
        }

        return bookingService.getCurrentUserBookings(userId, state);
    }

    // GET /bookings/owner?state={state}
    // Получение списка бронирований для всех вещей текущего пользователя.
    @GetMapping("/owner")
    public List<BookingDto> getOwnerItemsBookings(@RequestHeader(USER_ID) long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        BookingState state = BookingState.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Некорректный state: " + stateParam);
        }
        return bookingService.getOwnerItemsBookings(userId, state);
    }

}
