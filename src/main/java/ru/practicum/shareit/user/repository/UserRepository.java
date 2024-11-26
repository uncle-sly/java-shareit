package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> getAll();

    Optional<User> getById(Long id);

    User create(User user);

    User update(User user);

    void delete(Long id);

    boolean checkEmail(String email);

}
