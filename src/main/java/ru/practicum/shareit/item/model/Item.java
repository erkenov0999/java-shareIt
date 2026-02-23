package ru.practicum.shareit.item.model;

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
public class Item {
    private Long id;

    @NotBlank(message = "Наименование вещи не может являться пустым!")
    @Size(max = 50, message = "Данное поле не может превышать 50 символов")
    private String name;

    @NotBlank(message = "Описание вещи не может являться пустым!")
    @Size(max = 300, message = "Данное поле не может превышать 300 символов")
    private String description;

    @NotNull(message = "Статус аренды не может являться пустым!")
    private Boolean available;

    private Long ownerId;

    private String request;
}
