package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.EntityUpdateException;
import ru.practicum.shareit.item.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    public BookingDto create(Long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException(Item.class, " c ID = " + bookingDto.getItem() + ", не найден."));


        if (!item.getAvailable()) {
            throw new RuntimeException("Некорректный запрос.");
        }

        Booking newBooking = bookingMapper.toBooking(bookingDto);
        newBooking.setItem(item);
        newBooking.setBooker(user);
        newBooking.setStatus(BookingStatus.WAITING);

        return bookingMapper.toBookingDto(bookingRepository.save(newBooking));
    }

    public BookingDto update(Long userId, Long bookingId, boolean approved) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(Booking.class, " c ID = " + bookingId + ", не найден."));

        if (!booking.getItem().getOwner().getId().equals(userId) || !booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new EntityUpdateException(Booking.class, " Booker c ID = " + booking.getBooker().getId() +
                    ", User c ID = " + userId + " Не владелец! Редактировать вещь, может только ее владелец.");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    public BookingDto getById(Long userId, Long bookingId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(Booking.class, " c ID = " + bookingId + ", не найден."));

        if (!booking.getItem().getOwner().equals(user) && !booking.getBooker().equals(user)) {
            throw new ValidationException("Доступ к данным о бронировании запрещен.");
        }
        return bookingMapper.toBookingDto(booking);
    }

    public List<BookingDto> getCurrentUserBookings(Long userId, BookingState state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));

        return bookingMapper.toBookingDtos(bookingRepository.findBookingsByStateAndUserId(user.getId(), String.valueOf(state)));
    }

    public List<BookingDto> getOwnerItemsBookings(Long userId, BookingState state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));

        List<Long> itemsId = itemRepository.findAllByOwner_Id(user.getId()); //findAllByOwner_Id(user.getId());
        List<Booking> bookings = bookingRepository.findAll();

        return bookingMapper.toBookingDtos(bookings.stream()
                .filter(booking -> itemsId.contains(booking.getBooker().getId())).toList());
    }

}
