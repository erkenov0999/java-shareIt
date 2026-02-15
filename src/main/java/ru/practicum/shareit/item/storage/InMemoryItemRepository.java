package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long itemId = 1L;

    @Override
    public void saveItem(Item item) {
        item.setId(generateItemId());
        items.put(item.getId(), item);
        log.info("Вещь с id {} сохранена", item.getId());
    }

    @Override
    public Item findItemById(Long id) {
        Item item = Optional.ofNullable(items.get(id))
                .orElseThrow(() -> {
                    log.info("Вещь с id {} не найдена", id);
                    return new IllegalArgumentException("Вещь с id " + id + " не найдена");
                });
        log.info("Вещь с id {} найдена", id);
        return item;
    }

    @Override
    public Map<Long, Item> findAllItems() {
        log.info("Запрошен список всех вещей, количество: {}", items.size());
        return items;
    }

    @Override
    public void updateItem(Item updateItem) {
        Long itemId = updateItem.getId();
        Optional.ofNullable(items.get(itemId))
                .orElseThrow(() -> {
                    log.info("Вещь с id {} не найдена, изменение данных не возможно", itemId);
                    return new IllegalArgumentException("Вещь с id " + itemId + " не найдена");
                });
        items.replace(itemId, updateItem);
        log.info("Вещь с id {} изменена", itemId);
    }

    @Override
    public void deleteItem(Item deleteItem) {
        Long itemId = deleteItem.getId();
        Optional.ofNullable(items.get(itemId))
                .orElseThrow(() -> {
                    log.info("Вещь с id {} не найдена, удаление вещи не возможно", itemId);
                    return new IllegalArgumentException("Вещь с id " + itemId + " не найдена");
                });
        items.remove(itemId);
        log.info("Вещь с id {} удалена", itemId);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        String lowerText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable() != null && item.getAvailable())
                .filter(item -> (item.getName() != null && item.getName().toLowerCase().contains(lowerText)) ||
                        (item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerText)))
                .toList();
    }

    private Long generateItemId() {
        Long createItemId = itemId;
        itemId++;
        return createItemId;
    }
}
