package ru.practicum.shareit.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.EntityUpdateException;
import ru.practicum.shareit.item.exception.ValidationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utility.Constants.USER_ID;

@WebMvcTest(excludeAutoConfiguration = ErrorHandlingControllerAdvice.class, controllers = BookingController.class)
@AutoConfigureMockMvc
class ErrorHandlingControllerAdviceTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Long bookingId;
    private Long userId;

    @Test
    void handleEntityNotFoundException() throws Exception {
        bookingId = 1L;
        userId = 1L;

        when(bookingService.getById(userId, bookingId))
                .thenThrow(new EntityNotFoundException(Booking.class, " не найден."));

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Booking не найден."));
    }

    @Test
    void handleAnyException() throws Exception {
        bookingId = 1L;
        userId = 1L;

        when(bookingService.getById(userId, bookingId))
                .thenThrow(new RuntimeException("INTERNAL_SERVER_ERROR"));

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"));
    }

    @Test
    void handleEntityUpdateException() throws Exception {
        bookingId = 1L;
        userId = 1L;

        when(bookingService.update(userId, bookingId, true))
                .thenThrow(new EntityUpdateException(Booking.class, " не найден."));

        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", bookingId)
                        .header(USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Booking не найден."));
    }

    @Test
    void handleValidationException() throws Exception {
        userId = 1L;

        when(bookingService.create(eq(userId), any()))
                .thenThrow(new ValidationException("Validation failed"));

        mockMvc.perform(post("/bookings")
                        .header(USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookingDto())))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }

}