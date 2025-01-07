package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDtoTest {

    private final JacksonTester<ItemDto> json;

    @Test
    void shouldSerialize() throws Exception {
        LocalDateTime lastBooking = LocalDateTime.now().minusDays(1);
        LocalDateTime nextBooking = LocalDateTime.now().plusDays(1);
        CommentDto comment = new CommentDto(1L, "nice!", "User", LocalDateTime.now());
        ItemDto itemDto = new ItemDto(
                1L,
                "ItemName",
                "Description",
                true,
                lastBooking,
                nextBooking,
                List.of(comment),
                2L
        );

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.name")
                .hasJsonPath("$.description")
                .hasJsonPath("$.available")
                .hasJsonPath("$.lastBooking")
                .hasJsonPath("$.nextBooking")
                .hasJsonPath("$.comments")
                .hasJsonPath("$.requestId");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(itemDto.getId()));

        assertThat(result).extractingJsonPathStringValue("$.name")
                .satisfies(name -> assertThat(name).isEqualTo(itemDto.getName()));

        assertThat(result).extractingJsonPathStringValue("$.description")
                .satisfies(description -> assertThat(description).isEqualTo(itemDto.getDescription()));

        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .satisfies(available -> assertThat(available).isEqualTo(itemDto.getAvailable()));

        assertThat(result).extractingJsonPathValue("$.lastBooking")
                .satisfies(booking -> assertThat(booking.toString()).isEqualTo(itemDto.getLastBooking().toString()));

        assertThat(result).extractingJsonPathValue("$.nextBooking")
                .satisfies(booking -> assertThat(booking.toString()).isEqualTo(itemDto.getNextBooking().toString()));

        assertThat(result).extractingJsonPathArrayValue("$.comments")
                .hasSize(1);

        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(comment.getId()));

        assertThat(result).extractingJsonPathStringValue("$.comments[0].text")
                .satisfies(text -> assertThat(text).isEqualTo(comment.getText()));

        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName")
                .satisfies(author -> assertThat(author).isEqualTo(comment.getAuthorName()));

        assertThat(result).extractingJsonPathNumberValue("$.requestId")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(itemDto.getRequestId()));
    }


}