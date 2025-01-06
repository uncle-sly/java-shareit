package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestRepository itemRequestRepository;


    public ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " c ID = " + userId + ", не найден."));

        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setUser(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    public List<ItemRequestDto> getUserRequestsList(Long userId) {

        List<ItemRequest> itemRequests = itemRequestRepository.findItemRequestByUserIdOrderByCreatedDesc(userId);
        List<Long> itemRequestIds = itemRequests.stream().map(ItemRequest::getId).toList();
        List<Item> items = itemRepository.findByRequestIdIn(itemRequestIds);
        List<ItemRequestDto> itemRequestDtos = itemRequestMapper.toItemRequestDtoList(itemRequests);

        itemRequests.stream()
                .map(itemRequestMapper::toItemRequestDto)
                .forEach(itemRequestDto -> itemRequestDto.setItems(items.stream()
                        .filter(item -> item.getId().equals(itemRequestDto.getId())).map(itemMapper::toItemAnswerDto).toList()));

        return itemRequestDtos;
    }

    public List<ItemRequestDto> getAll(Long userId) {
        return itemRequestMapper.toItemRequestDtoList(itemRequestRepository.findItemRequestByUserIdNotOrderByCreatedDesc(userId));
    }

    public ItemRequestDto getById(Long userId, Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(ItemRequest.class, " c ID = " + requestId + ", не найден."));
        List<Item> items = itemRepository.findByRequestId(requestId);

        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(itemMapper.toItemAnswerDtoList(items));
        return itemRequestDto;
    }


}
