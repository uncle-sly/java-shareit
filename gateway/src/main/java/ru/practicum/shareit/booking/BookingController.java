package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;


import static ru.practicum.shareit.utility.Constants.USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

/*
	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}
*/

	// GET /bookings?state={state}
	// Получение списка всех бронирований текущего пользователя.
	@GetMapping
	public ResponseEntity<Object> getCurrentUserBookings(@RequestHeader(USER_ID) long userId,
														 @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {

		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Некорректный state: " + stateParam));

		return bookingClient.getCurrentUserBookings(userId, state);
	}

	// GET /bookings/owner?state={state}
	// Получение списка бронирований для всех вещей текущего пользователя.
	@GetMapping("/owner")
	public ResponseEntity<Object> getOwnerItemsBookings(@RequestHeader(USER_ID) long userId,
														@RequestParam(name = "state", defaultValue = "ALL") String stateParam) {

		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Некорректный state: " + stateParam));

		return bookingClient.getOwnerItemsBookings(userId, state);
	}

/*
	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
										   @RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}
*/

	//После создания запрос находится в статусе WAITING — «ожидает подтверждения».
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> create(@RequestHeader(USER_ID) long userId,
										 @Valid @RequestBody BookItemRequestDto requestDto) {
		return bookingClient.create(userId, requestDto);
	}

	//    PATCH /bookings/{bookingId}?approved={approved}
	//    Может быть выполнено только владельцем вещи. Затем статус бронирования становится либо APPROVED, либо REJECTED.
	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> update(@RequestHeader(USER_ID) long userId,
										 @PathVariable("bookingId") long bookingId,
										 @RequestParam("approved") boolean approved) {

		return bookingClient.update(userId, bookingId, approved);
	}
/*
	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
											 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}*/

	// GET /bookings/{bookingId}
	// Получение данных о конкретном бронировании (включая его статус). Может быть выполнено либо автором бронирования,
	// либо владельцем вещи, к которой относится бронирование.
	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getById(@RequestHeader(USER_ID) long userId,
										  @PathVariable("bookingId") long bookingId) {

		return bookingClient.getById(userId, bookingId);
	}


}
