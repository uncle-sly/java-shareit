package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnersItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utility.Constants.USER_ID;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ItemDto itemDto;
    private CommentDto commentDto;
    private OwnersItemDto ownersItemDto;


    @BeforeEach
    void setUp() {
        itemDto = new ItemDto(
                1L,
                "Test Item",
                "Description",
                true, // available
                null,
                null,
                List.of(),
                null
        );
        commentDto = new CommentDto(1L, "Great item", "Mike", null);
        ownersItemDto = new OwnersItemDto(
                1L,
                "Test Item",
                "Description of test item",
                true,
                null,
                null,
                List.of(commentDto)
        );
    }


    @Test
    void getOwnersItems() throws Exception {
        when(itemService.getOwnersItems(1L)).thenReturn(List.of(ownersItemDto));
        mockMvc.perform(get("/items")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(ownersItemDto))));
    }

    @Test
    void getById() throws Exception {
        when(itemService.getById(1L, 1L)).thenReturn(itemDto);
        mockMvc.perform(get("/items/1")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }


    @Test
    void getSearchedItems() throws Exception {
        when(itemService.getSearchedItems(1L, "Test")).thenReturn(List.of(itemDto));
        mockMvc.perform(get("/items/search")
                        .header(USER_ID, 1L)
                        .param("text", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemDto))));
    }

    @Test
    void create() throws Exception {
        when(itemService.create(eq(1L), any(ItemDto.class))).thenReturn(itemDto);
        mockMvc.perform(post("/items")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    @Test
    void update() throws Exception {
        when(itemService.update(eq(1L), eq(1L), any(ItemDto.class))).thenReturn(itemDto);
        mockMvc.perform(patch("/items/1")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    @Test
    void createComment() throws Exception {
        when(itemService.createComment(eq(1L), eq(1L), any(CommentDto.class))).thenReturn(commentDto);
        mockMvc.perform(post("/items/1/comment")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentDto)));
    }

}