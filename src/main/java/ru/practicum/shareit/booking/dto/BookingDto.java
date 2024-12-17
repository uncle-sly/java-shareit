package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@StartBeforeEnd
public class BookingDto implements StartEnd {

    private Long id;

    @FutureOrPresent
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    private BookingStatus status;
    private Long itemId;
    private ItemDto item;
    private BookerDto booker;

    public record ItemDto(Long id, String name) {
    }

    public record BookerDto(Long id) {
    }

}
