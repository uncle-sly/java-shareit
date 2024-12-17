package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = """
                   SELECT b.id, b.start_date, b.end_date, b.item_id, b.user_id, b.status
                   FROM bookings b
                   WHERE b.user_id = :userId
                   AND (
                       :state = 'ALL' OR
                       (:state = 'REJECTED' AND b.status = 'REJECTED') OR
                       (:state = 'WAITING' AND b.status = 'WAITING') OR
                       (:state = 'CURRENT' AND b.start_date <= CURRENT_TIMESTAMP AND b.end_date >= CURRENT_TIMESTAMP) OR
                       (:state = 'FUTURE' AND b.start_date > CURRENT_TIMESTAMP) OR
                       (:state = 'PAST' AND b.end_date < CURRENT_TIMESTAMP)
                   )
            """, nativeQuery = true)
    List<Booking> findBookingsByStateAndUserId(@Param("userId") Long userId, @Param("state") String state);

    Optional<Booking> findOneByItemIdAndBookerIdAndStatus(Long itemId, Long userId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId ORDER BY b.start DESC")
    List<Booking> findAllByItemIdOrderByStartDateDesc(@Param("itemId") Long itemId);

}

