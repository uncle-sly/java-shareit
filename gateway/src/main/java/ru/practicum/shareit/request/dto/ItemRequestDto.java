package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.OnCreate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequestDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Size(max = 256, message = "Описание не может быть длиннее 256 символов.")
    @NotBlank(groups = OnCreate.class, message = "description не должен быть null, должен содержать хотя бы один непробельный символ")
    private String description;

    private UserDto user;

    private LocalDateTime created;

}
