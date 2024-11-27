package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
    // Создаем экземпляр
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(User user);

    List<UserDto> toUserDtoList(List<User> users);

    User toUser(UserDto userDto);

}
