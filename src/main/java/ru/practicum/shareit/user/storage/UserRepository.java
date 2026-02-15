package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Map;

public interface UserRepository {
    void saveUser(User user);
    User findUserById(Long id);
    User findUserByEmail(String email);
    Map<Long, User> findAllUsers();
    void updateUser(User user);
    void deleteUser(User user);
    void validateEmail(String email);
}