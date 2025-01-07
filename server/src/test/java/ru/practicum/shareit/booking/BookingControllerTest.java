package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.utility.Constants.USER_ID;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto(
                1L,
                null,
                null,
                BookingStatus.WAITING,
                1L,
                new BookingDto.ItemDto(1L, "Test Item"),
                new BookingDto.BookerDto(1L)
        );
    }

    @Test
    void createBooking() throws Exception {
        when(bookingService.create(1L, bookingDto)).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header(USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isCreated()) // 201
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));
    }

    @Test
    void updateBooking() throws Exception {
        when(bookingService.update(1L, 1L, true)).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/1?approved=true")
                .header(USER_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));
    }

    @Test
    void getById() throws Exception {
        when(bookingService.getById(1L, 1L)).thenReturn(bookingDto);
        mockMvc.perform(get("/bookings/1")
                .header(USER_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));
    }

    @Test
    void getCurrentUserBookings() throws Exception {
        when(bookingService.getCurrentUserBookings(eq(1L), any())).thenReturn(List.of(bookingDto));
        mockMvc.perform(get("/bookings")
                .header(USER_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingDto))));
    }

    @Test
    void getCurrentUserBookingsWithState() throws Exception {
        when(bookingService.getCurrentUserBookings(eq(1L), eq(BookingState.WAITING)))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header(USER_ID, 1L)
                        .param("state", "WAITING")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingDto))));

        when(bookingService.getCurrentUserBookings(eq(1L), eq(BookingState.REJECTED)))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header(USER_ID, 1L)
                        .param("state", "REJECTED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingDto))));
    }

    @Test
    void getCurrentUserBookingsWithInvalidState() throws Exception {
        String invalidState = "INVALID_STATE";

        mockMvc.perform(get("/bookings")
                        .header(USER_ID, 1L)
                        .param("state", invalidState)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Некорректный state: " + invalidState));
    }


    @Test
    void getOwnerItemsBookings() throws Exception {
        when(bookingService.getOwnerItemsBookings(eq(1L), any())).thenReturn(List.of(bookingDto));
        mockMvc.perform(get("/bookings/owner")
                .header(USER_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingDto))));
    }

}