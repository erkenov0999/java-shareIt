package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemRepository {

    void saveItem(Item item);

    Item findItemById(Long id);

    Map<Long, Item> findAllItems();

    void updateItem(Item item);

    void deleteItem(Item item);

    List<Item> searchItems(String text);
}