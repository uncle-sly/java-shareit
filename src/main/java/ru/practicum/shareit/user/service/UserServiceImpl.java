package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.exception.UserEmailExistedException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public List<UserDto> getAll() {
        return userMapper.toUserDtoList(userRepository.getAll());
    }

    public UserDto getById(Long id) {
        return userMapper.toUserDto(userRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + id + ", не найден.")));
    }

    public UserDto create(UserDto userDto) {

        if (userRepository.checkEmail(userDto.getEmail()))
            throw new UserEmailExistedException("Такой email уже используется.");

        return userMapper.toUserDto(userRepository.create(userMapper.toUser(userDto)));
    }

    public UserDto update(Long id, UserDto userDto) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + id + ", не найден."));

        if (!user.getEmail().equals(userDto.getEmail()) && userRepository.checkEmail(userDto.getEmail()))
            throw new UserEmailExistedException("Такой email уже используется.");

        final String name = userDto.getName();
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }

        final String email = userDto.getEmail();
        if (email != null && !email.isBlank()) {
            userRepository.updateEmails(user, userDto);
            user.setEmail(email);
        }

        return userMapper.toUserDto(user);
    }

    public void delete(Long id) {
        userRepository.delete(id);
    }
}