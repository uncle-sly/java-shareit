package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemAnswerDtoJsonTest {
    private final JacksonTester<ItemAnswerDto> json;

    @Test
    void shouldSerialize() throws Exception {
        ItemAnswerDto itemAnswerDto = new ItemAnswerDto(1L, "Item Name", 2L);

        JsonContent<ItemAnswerDto> result = json.write(itemAnswerDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.name")
                .hasJsonPath("$.ownerId");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(itemAnswerDto.getId()));

        assertThat(result).extractingJsonPathStringValue("$.name")
                .satisfies(name -> assertThat(name).isEqualTo(itemAnswerDto.getName()));

        assertThat(result).extractingJsonPathNumberValue("$.ownerId")
                .satisfies(ownerId -> assertThat(ownerId.longValue()).isEqualTo(itemAnswerDto.getOwnerId()));
    }
}
