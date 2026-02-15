package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;

    @NotBlank(message = "Имя пользователя не может являться пустым!")
    @Size(max = 50, message = "Максимальное колличесвто символов не должно превышать 50!")
    private String username;

    @NotBlank(message = "Email не может являться пустым!")
    @Email(message = "Введите корректный email!")
    private String email;
}