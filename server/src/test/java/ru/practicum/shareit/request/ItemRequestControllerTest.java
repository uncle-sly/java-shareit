package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.utility.Constants.USER_ID;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ItemRequestDto itemRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Ivan", "ivan@example.ru");

        itemRequestDto = new ItemRequestDto(
                1L,
                "looking for a hammer",
                user,
                null,
                List.of()
        );
    }

    @Test
    void create() throws Exception {
        when(itemRequestService.create(1L, itemRequestDto)).thenReturn(itemRequestDto);
        mockMvc.perform(post("/requests")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isCreated());

    }

    @Test
    void getUserRequestsList() throws Exception {
        when(itemRequestService.getUserRequestsList(1L)).thenReturn(List.of(itemRequestDto));
        mockMvc.perform(get("/requests")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemRequestDto))));
    }

    @Test
    void getAllRequestsList() throws Exception {
        when(itemRequestService.getAll(1L)).thenReturn(List.of(itemRequestDto));
        mockMvc.perform(get("/requests/all")
                .header(USER_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemRequestDto))));
    }

    @Test
    void getRequestById() throws Exception {
        when(itemRequestService.getById(1L, 1L)).thenReturn(itemRequestDto);
        mockMvc.perform(get("/requests/1")
                .header(USER_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("looking for a hammer"));
    }

}