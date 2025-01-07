package ru.practicum.shareit.booking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class BookingServiceImplTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    private final EntityManager entityManager;

    private LocalDateTime now;
    private LocalDateTime nowPlusDay;
    private BookingDto.BookerDto booker;
    private BookingDto.ItemDto item;
    private UserDto userDto;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        nowPlusDay = now.plusDays(1);
        userDto = userService.create(new UserDto(1L, "Ivan", "Ivanov@gmail.ru"));
        itemDto = itemService.create(userDto.getId(), new ItemDto(2L,"Item", "description",
                true, null, null, null, 5L));
        item = new BookingDto.ItemDto(itemDto.getId(), itemDto.getName());
        booker = new BookingDto.BookerDto(userDto.getId());
    }

    @Test
    void shouldCreateBooking() {

        BookingDto bookingDto = new BookingDto(1L, now, nowPlusDay, BookingStatus.WAITING, itemDto.getId(), item, booker);
        bookingDto = bookingService.create(userDto.getId(), bookingDto);

        TypedQuery<Booking> query = entityManager.createQuery("select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingDto.getId()).getSingleResult();

        assertThat(booking, notNullValue());
        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(booking.getStart(), equalTo(bookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(bookingDto.getEnd()));
    }

    @Test
    void shouldUpdateBooking() {

        BookingDto bookingDto = new BookingDto(1L, now, nowPlusDay, BookingStatus.WAITING, itemDto.getId(), item, booker);
        bookingDto = bookingService.create(userDto.getId(), bookingDto);

        BookingDto update = bookingService.update(userDto.getId(), bookingDto.getId(), true);

        assertThat(update, notNullValue());
        assertThat(update.getId(), equalTo(bookingDto.getId()));
        assertThat(update.getStatus(), equalTo(BookingStatus.APPROVED));
        assertThat(update.getStart(), equalTo(bookingDto.getStart()));
        assertThat(update.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(update.getBooker().id(), equalTo(bookingDto.getBooker().id()));
        assertThat(update.getItem().id(), equalTo(bookingDto.getItem().id()));
        assertThat(update.getItem().name(), equalTo(bookingDto.getItem().name()));

        BookingDto bookingDto2 = new BookingDto(2L, now, nowPlusDay, BookingStatus.WAITING, itemDto.getId(), item, booker);
        bookingDto2 = bookingService.create(userDto.getId(), bookingDto2);

        BookingDto update2 = bookingService.update(userDto.getId(), bookingDto2.getId(), false);
        assertThat(update2, notNullValue());
        assertThat(update2.getId(), equalTo(bookingDto2.getId()));
        assertThat(update2.getStatus(), equalTo(BookingStatus.REJECTED));
        assertThat(update2.getStart(), equalTo(bookingDto2.getStart()));
        assertThat(update2.getEnd(), equalTo(bookingDto2.getEnd()));
        assertThat(update2.getBooker().id(), equalTo(bookingDto2.getBooker().id()));
        assertThat(update2.getItem().id(), equalTo(bookingDto2.getItem().id()));
        assertThat(update2.getItem().name(), equalTo(bookingDto2.getItem().name()));
    }

    @Test
    void shouldGetById() {

        BookingDto bookingDto = new BookingDto(1L, now, nowPlusDay, BookingStatus.WAITING, itemDto.getId(), item, booker);
        bookingDto = bookingService.create(userDto.getId(), bookingDto);

        BookingDto getResult = bookingService.getById(userDto.getId(), bookingDto.getId());

        assertThat(getResult, notNullValue());
        assertThat(getResult.getId(), equalTo(bookingDto.getId()));
        assertThat(getResult.getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(getResult.getStart(), equalTo(bookingDto.getStart()));
        assertThat(getResult.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(getResult.getBooker(), equalTo(bookingDto.getBooker()));
    }

    @Test
    void shouldGetCurrentUserBookings() {
        BookingDto bookingDto = new BookingDto(1L, now, nowPlusDay, BookingStatus.WAITING, itemDto.getId(), item, booker);
        bookingDto = bookingService.create(userDto.getId(), bookingDto);

        List<BookingDto> getResult = bookingService.getCurrentUserBookings(userDto.getId(), BookingState.ALL);
        assertThat(getResult, notNullValue());
        assertThat(getResult.size(), equalTo(1));
        assertThat(getResult.getFirst().getId(), equalTo(bookingDto.getId()));
        assertThat(getResult.getFirst().getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(getResult.getFirst().getStart(), equalTo(bookingDto.getStart()));
        assertThat(getResult.getFirst().getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(getResult.getFirst().getBooker(), equalTo(bookingDto.getBooker()));

        getResult = bookingService.getCurrentUserBookings(userDto.getId(), BookingState.REJECTED);
        assertThat(getResult, notNullValue());
        assertThat(getResult.size(), equalTo(0));

        getResult = bookingService.getCurrentUserBookings(userDto.getId(), BookingState.WAITING);
        assertThat(getResult, notNullValue());
        assertThat(getResult.size(), equalTo(1));
        assertThat(getResult.getFirst().getId(), equalTo(bookingDto.getId()));
        assertThat(getResult.getFirst().getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(getResult.getFirst().getStart(), equalTo(bookingDto.getStart()));
        assertThat(getResult.getFirst().getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(getResult.getFirst().getBooker(), equalTo(bookingDto.getBooker()));

        getResult = bookingService.getCurrentUserBookings(userDto.getId(), BookingState.CURRENT);
        assertThat(getResult, notNullValue());
        assertThat(getResult.size(), equalTo(1));

        getResult = bookingService.getCurrentUserBookings(userDto.getId(), BookingState.FUTURE);
        assertThat(getResult, notNullValue());
        assertThat(getResult.size(), equalTo(0));

        getResult = bookingService.getCurrentUserBookings(userDto.getId(), BookingState.PAST);
        assertThat(getResult, notNullValue());
        assertThat(getResult.size(), equalTo(0));
    }

    @Test
    void shouldGetOwnerItemsBookings() {
        BookingDto bookingDto = new BookingDto(1L, now, nowPlusDay, BookingStatus.WAITING, itemDto.getId(), item, booker);
        bookingDto = bookingService.create(userDto.getId(), bookingDto);

        List<BookingDto> getResult = bookingService.getOwnerItemsBookings(userDto.getId(), BookingState.ALL);

        assertThat(getResult, notNullValue());
        assertThat(getResult.size(), equalTo(1));
        assertThat(getResult.getFirst().getId(), equalTo(bookingDto.getId()));
    }

}