package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestDtoJsonTest {
    private final JacksonTester<ItemRequestDto> json;

    @Test
    void shouldSerialize() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "John", "john@example.ru");
        ItemAnswerDto item = new ItemAnswerDto(2L, "Item Name", 1L);

        ItemRequestDto itemRequestDto = new ItemRequestDto(
                1L,
                "Request description",
                user,
                now,
                List.of(item)
        );

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.description")
                .hasJsonPath("$.user")
                .hasJsonPath("$.created")
                .hasJsonPath("$.items");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(itemRequestDto.getId()));

        assertThat(result).extractingJsonPathStringValue("$.description")
                .satisfies(description -> assertThat(description).isEqualTo(itemRequestDto.getDescription()));

        assertThat(result).extractingJsonPathValue("$.user")
                .satisfies(userJson -> assertThat(userJson.toString()).contains("John", "john@example.ru"));

        assertThat(result).extractingJsonPathArrayValue("$.items")
                .satisfies(items -> {
                    assertThat(items).hasSize(1);
                    assertThat(items.getFirst().toString()).contains("Item Name", "2", "1");
                });
    }
}
