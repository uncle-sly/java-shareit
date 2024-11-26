package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.exception.UserEmailExistedException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final UserRepository userRepository;

    public List<UserDto> getAll() {

        return userMapper.toItemDTOList(userRepository.getAll());
    }

   public UserDto getById(Long id) {

       return userMapper.toUserDto(userRepository.getById(id)
               .orElseThrow(() -> new UserNotFoundException("Пользователь c ID - " + id + ", не найден.")));
   }

    public UserDto create(UserDto userDto) {

        if (userRepository.checkEmail(userDto.getEmail())) throw new UserEmailExistedException("Такой email уже используется.");
        return userMapper.toUserDto(userRepository.create(userMapper.toUser(userDto)));
    }

    public UserDto update(Long id, UserDto userDto) {
       User user = userRepository.getById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь c ID - " + id + ", не найден."));

        if (userRepository.checkEmail(userDto.getEmail())) throw new UserEmailExistedException("Такой email уже используется.");


       user.setName(userDto.getName());
       user.setEmail(userDto.getEmail());

       return userMapper.toUserDto(userRepository.update(user));
    }

    public void delete(Long id) {
    userRepository.delete(id);
    }


}
