package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    private Long userId = 0L;


    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> getById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public User create(User user) {
        user.setId(generateUserId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }


    public void updateEmails(User user, UserDto userDto) {
        if (!user.getEmail().equals(userDto.getEmail())) {
            emails.remove(user.getEmail());
            emails.add(userDto.getEmail());
        }
    }

    public void delete(Long id) {
        emails.remove(users.remove(id).getEmail());
    }

    private long generateUserId() {
        return ++userId;
    }

    public boolean checkEmail(String email) {
        return emails.contains(email);
    }
}
