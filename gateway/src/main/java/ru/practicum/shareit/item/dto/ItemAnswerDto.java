package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemAnswerDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private Long id;

    @NotBlank(message = "name не должен быть null, должен содержать хотя бы один непробельный символ")
    private String name;

    @NotNull
    private Long ownerId;
}
