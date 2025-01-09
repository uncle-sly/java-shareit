package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class ItemRequestServiceImplTest {

    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final EntityManager entityManager;

    private UserDto userDto;
    private UserDto userDto2;
    private ItemRequestDto itemRequest;

    @BeforeEach
    void setUp() {
        userDto = userService.create(new UserDto(1L, "Ivan", "Ivanov@gmail.ru"));
        userDto2 = userService.create(new UserDto(2L, "Mike", "Mike@gmail.ru"));
        itemRequest = itemRequestService.create(userDto.getId(), new ItemRequestDto(null,"guitar", null, null, null));
    }

    @Test
    void shouldCreateItemRequest() {
        ItemRequestDto itemRequest2 = new ItemRequestDto(null,"piano", null, null, null);
        itemRequest2 = itemRequestService.create(userDto.getId(), itemRequest2);

        TypedQuery<ItemRequest> query = entityManager.createQuery("select i from ItemRequest as i where i.description = :description", ItemRequest.class);
        ItemRequest qItemRequest2 = query.setParameter("description", itemRequest2.getDescription()).getSingleResult();

        assertThat(qItemRequest2, notNullValue());
        assertThat(qItemRequest2.getDescription(), equalTo(itemRequest2.getDescription()));
    }

    @Test
    void shouldGetUserRequestsList() {
        ItemRequestDto itemRequest2 = new ItemRequestDto(null,"piano", null, null, null);
        itemRequest2 = itemRequestService.create(userDto.getId(), itemRequest2);

        List<ItemRequestDto> itemRequestDtos = itemRequestService.getUserRequestsList(userDto.getId());
        assertThat(itemRequestDtos.size(), equalTo(2));
        assertThat(itemRequestDtos.getFirst().getUser().getId(), equalTo(userDto.getId()));
        assertThat(itemRequestDtos.getFirst().getDescription(), equalTo(itemRequest2.getDescription()));
        assertThat(itemRequestDtos.getLast().getUser().getId(), equalTo(userDto.getId()));
        assertThat(itemRequestDtos.getLast().getDescription(), equalTo(itemRequest.getDescription()));
    }

    @Test
    void shouldGetAllItemRequests() {
        ItemRequestDto itemRequest3 = new ItemRequestDto(null,"piano", null, null, null);
        itemRequest3 = itemRequestService.create(userDto2.getId(), itemRequest3);

        List<ItemRequestDto> itemRequestDtos = itemRequestService.getAll(userDto.getId());
        assertThat(itemRequestDtos.size(), equalTo(1));
        assertThat(itemRequestDtos.getFirst().getDescription(), equalTo(itemRequest3.getDescription()));
        assertThat(itemRequestDtos.getFirst().getUser().getId(), equalTo(userDto2.getId()));
    }

    @Test
    void shouldGetItemRequestById() {
        ItemRequestDto itemRequestDto = itemRequestService.getById(userDto.getId(), itemRequest.getId());
        assertThat(itemRequestDto, notNullValue());
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(itemRequestDto.getUser().getId(), equalTo(userDto.getId()));
    }

}