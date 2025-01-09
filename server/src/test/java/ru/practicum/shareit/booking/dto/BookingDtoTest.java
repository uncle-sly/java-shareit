package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoJsonTest {
    private final JacksonTester<BookingDto> json;

    @Test
    void shouldSerialize() throws Exception {
        BookingDto.BookerDto bookerDto = new BookingDto.BookerDto(1L);
        BookingDto.ItemDto itemDto = new BookingDto.ItemDto(2L, "Item");
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now(), BookingStatus.APPROVED, 2L, itemDto, bookerDto);

        JsonContent<BookingDto> result = json.write(bookingDto);
        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.start")
                .hasJsonPath("$.end")
                .hasJsonPath("$.status")
                .hasJsonPath("$.itemId")
                .hasJsonPathValue("$.item.id")
                .hasJsonPathValue("$.item.name")
                .hasJsonPathValue("$.booker.id");

        assertThat(result).extractingJsonPathNumberValue("$.id").satisfies(id -> assertThat(id.longValue()).isEqualTo(bookingDto.getId()));
        assertThat(result).extractingJsonPathNumberValue("$.itemId").satisfies(itemId -> assertThat(itemId.longValue()).isEqualTo(bookingDto.getItemId()));

        assertThat(result).extractingJsonPathNumberValue("$.item.id").satisfies(id -> assertThat(id.longValue()).isEqualTo(itemDto.id()));
        assertThat(result).extractingJsonPathStringValue("$.item.name").satisfies(name -> assertThat(name).isEqualTo(itemDto.name()));

        assertThat(result).extractingJsonPathNumberValue("$.booker.id").satisfies(id -> assertThat(id.longValue()).isEqualTo(bookerDto.id()));
    }

}