package ru.practicum.shareit.user.storage;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class InMemoryUserRepository implements UserRepository {
    private Map<Long, User> users =  new HashMap<>();
    private Long userId = 1L;

    @Override
    public void saveUser(User user) {
        validateEmail(user.getEmail());
        user.setId(generateUserId());
        users.put(user.getId(), user);
        log.info("Новый пользователь сохранен в системе, присвоен id {}", user.getId());
    }

    @Override
    public User findUserById(Long id) {
        User user = Optional.ofNullable(users.get(id))
                .orElseThrow(() -> {
                    log.info("Пользователь с id {} не найден", id);
                    return new IllegalArgumentException("Пользователь с id " + id + " не найден");
                });

        log.info("Пользователь с id {} найден", id);
        return user;
    }

    @Override
    public Map<Long, User> findAllUsers() {
        log.info("Запрошен список всех пользователей, количество: {}", users.size());
        return users;
    }

    @Override
    public void updateUser(User updateUser) {
        Long userId = updateUser.getId();
        Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> {
                    log.info("Пользователь с id {} не найден, изменение данных не возможно", userId);
                    return new IllegalArgumentException("Пользователь с id " + userId + " не найден");
                });
        users.replace(userId, updateUser);
        log.info("Пользователь с id {} изменен", userId);
    }

    @Override
    public void deleteUser(User deleteUser) {
        Long userId = deleteUser.getId();
        Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> {
                    log.info("Пользователь с id {} не найден, удаление пользователя не возможно", userId);
                    return new IllegalArgumentException("Пользователь с id " + userId + " не найден");
                });
        users.remove(userId);
        log.info("Пользователь с id {} удален", userId);
    }

    private Long generateUserId() {
        Long createUserId = userId;
        userId++;
        return createUserId;
    }

    @Override
    public void validateEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                throw new RuntimeException("Данный email уже сохранен в системе!");
            }
        }
    }
}