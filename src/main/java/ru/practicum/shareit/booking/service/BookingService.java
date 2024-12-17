package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;


public interface BookingService {

    BookingDto create(Long userId, BookingDto bookingDto);

    BookingDto update(Long userId, Long bookingId, boolean approved);

    BookingDto getById(Long userId, Long bookingId);

    List<BookingDto> getCurrentUserBookings(Long userId, BookingState state);

    List<BookingDto> getOwnerItemsBookings(Long userId, BookingState state);

}
