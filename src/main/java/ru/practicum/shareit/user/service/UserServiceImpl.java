package ru.practicum.shareit.user.service;

import jakarta.validation.ValidationException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.InMemoryUserRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Getter
@Setter
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserServiceImpl(InMemoryUserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        validateUser(userDto);
        User user = userMapper.toUser(userDto);
        userRepository.saveUser(user);
        log.info("Создан новый пользователь с id {}", user.getId());
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto updateUserDto) {
        Long updatedUserId = updateUserDto.getId();
        if (updatedUserId == null) {
            throw new ValidationException("ID пользователя не может быть пустым!");
        }

        User existingUser = userRepository.findUserById(updatedUserId);

        // Обновляем только переданные поля
        if (updateUserDto.getName() != null) {
            existingUser.setUsername(updateUserDto.getName());
        }
        if (updateUserDto.getEmail() != null) {
            // Проверяем email только если он изменился
            if (!existingUser.getEmail().equals(updateUserDto.getEmail())) {
                validateEmail(updateUserDto.getEmail());
            }
            existingUser.setEmail(updateUserDto.getEmail());
        }

        userRepository.updateUser(existingUser);
        log.info("Пользователь с id {} изменен", updatedUserId);
        return userMapper.toUserDto(existingUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        if (userId == null || userRepository.findUserById(userId) == null) {
            throw new RuntimeException("Пользователь не найден!");
        }
        log.info("Поиск пользователя через DTO");
        return userMapper.toUserDto(userRepository.findUserById(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Вывод списка DTO пользователей");
        return userRepository.findAllUsers().values().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        if (id == null || userRepository.findUserById(id) == null) {
            throw new RuntimeException("Пользователь не найден!");
        }
        User deleteUser = userRepository.findUserById(id);
        userRepository.deleteUser(deleteUser);
        log.info("Удаление пользователя через DTO");
    }

    private void validateUser(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new ValidationException("Имя пользователя не может быть пустым!");
        }

        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new ValidationException("Email не может быть пустым!");
        }

        if (!userDto.getEmail().contains("@")) {
            throw new ValidationException("Некорректный формат email");
        }

        // Проверяем уникальность email только при создании нового пользователя
        validateEmail(userDto.getEmail());
    }

    private void validateEmail(String email) {
        try {
            userRepository.validateEmail(email);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
