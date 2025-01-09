package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class UserServiceImplTest {

    private final UserService userService;
    private final EntityManager entityManager;
    private UserDto userDto;
    private UserDto userDto2;

    @BeforeEach
    void setUp() {
        userDto = userService.create(new UserDto(1L, "Ivan", "Ivanov@gmail.ru"));
        userDto2 = userService.create(new UserDto(2L, "Mike", "Mike@gmail.ru"));
    }

    @Test
    void shouldGetAllUsers() {
        List<UserDto> users = userService.getAll();
        assertThat(users, notNullValue());
        assertThat(users.size(), equalTo(2));
        assertThat(users.getFirst().getId(), equalTo(userDto.getId()));
        assertThat(users.getLast().getId(), equalTo(userDto2.getId()));
    }

    @Test
    void shouldGetUserById() {
        UserDto user = userService.getById(userDto.getId());
        assertThat(user, notNullValue());
        assertThat(user.getId(), equalTo(userDto.getId()));
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));

    }


    @Test
    void shouldCreateUser() {
        UserDto newUser = new UserDto(3L, "Oleg", "Oleg@gmail.ru");
        newUser = userService.create(newUser);

        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.id = :id", User.class);
        User qUser = query.setParameter("id", newUser.getId()).getSingleResult();

        assertThat(qUser, notNullValue());
        assertThat(qUser.getId(), equalTo(newUser.getId()));
        assertThat(qUser.getName(), equalTo(newUser.getName()));
        assertThat(qUser.getEmail(), equalTo(newUser.getEmail()));
    }

    @Test
    void shouldUpdateUser() {
        UserDto newUser = new UserDto(3L, "Oleg", "Oleg@gmail.ru");
        newUser = userService.create(newUser);
        newUser.setName("German");
        newUser.setEmail("german@gmail.ru");
        userService.update(newUser.getId(), newUser);

        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.id = :id", User.class);
        User qUser = query.setParameter("id", newUser.getId()).getSingleResult();

        assertThat(qUser, notNullValue());
        assertThat(qUser.getId(), equalTo(newUser.getId()));
        assertThat(qUser.getName(), equalTo(newUser.getName()));
        assertThat(qUser.getEmail(), equalTo(newUser.getEmail()));
    }

    @Test
    void shouldDeleteUser() {
        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.id = :id", User.class);
        User qUser = query.setParameter("id", userDto.getId()).getSingleResult();
        assertThat(qUser, notNullValue());
        assertThat(qUser.getId(), equalTo(userDto.getId()));

        userService.delete(userDto.getId());
        query = entityManager.createQuery("select u from User u where u.id = :id", User.class);
        List<User> qUsers = query.setParameter("id", userDto.getId()).getResultList();
        assertThat(qUsers.isEmpty(), equalTo(true));
    }

}