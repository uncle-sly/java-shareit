package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentDtoJsonTest {
    private final JacksonTester<CommentDto> json;

    @Test
    void shouldSerialize() throws Exception {
        LocalDateTime createdTime = LocalDateTime.now();
        CommentDto commentDto = new CommentDto(1L, "cheap item", "Peter", createdTime);

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.text")
                .hasJsonPath("$.authorName")
                .hasJsonPath("$.created");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(commentDto.getId()));

        assertThat(result).extractingJsonPathStringValue("$.text")
                .satisfies(text -> assertThat(text).isEqualTo(commentDto.getText()));

        assertThat(result).extractingJsonPathStringValue("$.authorName")
                .satisfies(authorName -> assertThat(authorName).isEqualTo(commentDto.getAuthorName()));

        assertThat(result).extractingJsonPathValue("$.created")
                .satisfies(created -> assertThat(created.toString()).isEqualTo(commentDto.getCreated().toString()));
    }

}