package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailExistedException;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.utility.Constants.USER_ID;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private UserDto userDto;


    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "Pavel", "pavel@example.ru");
    }

    @Test
    void getAll() throws Exception {
        when(userService.getAll()).thenReturn(List.of(userDto));
        mockMvc.perform(get("/users")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(userDto))));

        verify(userService, times(1)).getAll();
    }

    @Test
    void getById() throws Exception {
        when(userService.getById(1L)).thenReturn(userDto);
        mockMvc.perform(get("/users/1")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));

        verify(userService, times(1)).getById(1L);
    }

    @Test
    void create() throws Exception {
        when(userService.create(any(UserDto.class))).thenReturn(userDto);
        mockMvc.perform(post("/users")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));

        verify(userService, times(1)).create(any(UserDto.class));
    }

    @Test
    void createWithException() throws Exception {
        when(userService.create(any(UserDto.class)))
                .thenThrow(new UserEmailExistedException("Такой email уже используется."));

        mockMvc.perform(post("/users")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Такой email уже используется."));

        verify(userService, times(1)).create(any(UserDto.class));
    }


    @Test
    void update() throws Exception {
        when(userService.update(eq(1L), any(UserDto.class))).thenReturn(userDto);
        mockMvc.perform(patch("/users/1")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));

        verify(userService, times(1)).update(eq(1L), any(UserDto.class));
    }


    @Test
    void delete() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).delete(1L);
    }

}