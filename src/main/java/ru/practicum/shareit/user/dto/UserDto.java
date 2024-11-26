package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.validation.OnCreate;
import ru.practicum.shareit.user.validation.OnUpdate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull(groups = OnUpdate.class)
    private Long id;

    @NotEmpty(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    private String name;

    @NotEmpty(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "некорретный email адрес", groups = OnCreate.class)
    private String email;

}
