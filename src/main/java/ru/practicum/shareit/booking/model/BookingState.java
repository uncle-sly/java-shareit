package ru.practicum.shareit.booking.model;

public enum BookingState {
    ALL,
    CURRENT,
    FUTURE,
    PAST,
    REJECTED,
    WAITING;


    public static BookingState from(String state) {
        for (BookingState value : BookingState.values()) {
            if (value.name().equalsIgnoreCase(state)) {
                return value;
            }
        }
        return null;
    }

}
