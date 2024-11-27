package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.OnCreate;
import ru.practicum.shareit.validation.OnUpdate;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull(groups = OnUpdate.class)
    private Long id;

    @NotBlank(groups = OnCreate.class, message = "name не может состоять из пробелов")
    @NotEmpty(groups = OnCreate.class, message = "name не может быть пустым")
    private String name;

    @Size(max = 256, message = "Описание не может быть длиннее 256 символов.")
    @NotBlank(groups = OnCreate.class, message = "name не может состоять из пробелов")
    @NotEmpty(groups = OnCreate.class, message = "name не может быть пустым")
    private String description;

    @BooleanFlag
    @NotNull(groups = OnCreate.class, message = "name не может быть пустым")
    private Boolean available;

}
