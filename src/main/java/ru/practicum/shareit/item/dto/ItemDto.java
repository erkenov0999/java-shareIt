package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название вещи не может быть пустым")
    @Size(max = 50, message = "Название вещи не может превышать 50 символов")
    private String name;

    @NotBlank(message = "Описание вещи не может быть пустым")
    @Size(max = 300, message = "Описание вещи не может превышать 300 символов")
    private String description;

    @NotNull(message = "Статус доступности не может быть пустым")
    private Boolean available;

    private Long ownerId;

    private String request;
}
