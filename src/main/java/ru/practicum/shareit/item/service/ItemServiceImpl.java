package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.InMemoryItemRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;

    public ItemServiceImpl(InMemoryItemRepository itemRepository, ItemMapper itemMapper, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        // Проверяем существование пользователя
        try {
            userRepository.findUserById(ownerId);
        } catch (Exception e) {
            throw new RuntimeException("Пользователь с id " + ownerId + " не найден");
        }

        Item item = itemMapper.toItem(itemDto);
        item.setOwnerId(ownerId);
        itemRepository.saveItem(item);
        log.info("Создана новая вещь с id {} для владельца {}", item.getId(), ownerId);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        Item existingItem = itemRepository.findItemById(itemId);

        if (!existingItem.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("Только владелец может редактировать вещь!");
        }

        // Обновляем только переданные поля
        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        itemRepository.updateItem(existingItem);
        log.info("Обновлена вещь с id {} владельцем {}", itemId, ownerId);
        return itemMapper.toItemDto(existingItem);
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = itemRepository.findItemById(id);
        log.info("Получена вещь с id {}", id);
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Long ownerId) {
        List<ItemDto> items = itemRepository.findAllItems().values().stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("Получен список всех вещей владельца {}, количество: {}", ownerId, items.size());
        return items;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<ItemDto> items = itemRepository.searchItems(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("Выполнен поиск по тексту '{}', найдено вещей: {}", text, items.size());
        return items;
    }
}
