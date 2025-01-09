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

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class OwnersItemDtoJsonTest {
    private final JacksonTester<OwnersItemDto> json;

    @Test
    void shouldSerialize() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        CommentDto comment = new CommentDto(1L, "very good item", "Mike", now);
        OwnersItemDto ownersItemDto = new OwnersItemDto(
                1L,
                "Item Name",
                "item description",
                true,
                now.minusDays(1),
                now.plusDays(1),
                List.of(comment)
        );

        JsonContent<OwnersItemDto> result = json.write(ownersItemDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.name")
                .hasJsonPath("$.description")
                .hasJsonPath("$.available")
                .hasJsonPath("$.lastBooking")
                .hasJsonPath("$.nextBooking")
                .hasJsonPath("$.comments");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(ownersItemDto.getId()));

        assertThat(result).extractingJsonPathStringValue("$.name")
                .satisfies(name -> assertThat(name).isEqualTo(ownersItemDto.getName()));

        assertThat(result).extractingJsonPathStringValue("$.description")
                .satisfies(description -> assertThat(description).isEqualTo(ownersItemDto.getDescription()));

        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .satisfies(available -> assertThat(available).isEqualTo(ownersItemDto.getAvailable()));

        assertThat(result).extractingJsonPathValue("$.lastBooking")
                .satisfies(lastBooking -> assertThat(lastBooking.toString()).isEqualTo(ownersItemDto.getLastBooking().toString()));

        assertThat(result).extractingJsonPathValue("$.nextBooking")
                .satisfies(nextBooking -> assertThat(nextBooking.toString()).isEqualTo(ownersItemDto.getNextBooking().toString()));

        assertThat(result).extractingJsonPathArrayValue("$.comments")
                .satisfies(comments -> {
                    assertThat(comments).hasSize(1);
                    assertThat(comments.getFirst().toString()).contains("very good item", "Mike");
                });
    }

}
