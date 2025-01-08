package ru.practicum.shareit.item.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnersItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class ItemServiceImplTest {
    private final ItemService itemService;
    private final BookingService bookingService;
    private final UserService userService;

    private final EntityManager entityManager;

    private LocalDateTime was;
    private LocalDateTime wasMinus;
    private UserDto userDto;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private BookingDto.BookerDto booker;
    private BookingDto.ItemDto item;


    @BeforeEach
    void setUp() {
        was = LocalDateTime.now().minusDays(15);
        wasMinus = LocalDateTime.now().minusDays(17);

        userDto = userService.create(new UserDto(1L, "Ivan", "Ivanov@gmail.ru"));

        itemDto = itemService.create(userDto.getId(), new ItemDto(2L,"Fender", "electric guitar",
                true, null, null, null, 5L));

        commentDto = new CommentDto(null, "nice item", "Peter", LocalDateTime.now().minusDays(5));

        item = new BookingDto.ItemDto(itemDto.getId(), itemDto.getName());
        booker = new BookingDto.BookerDto(userDto.getId());
    }


    @Test
    void shouldGetOwnersItems() {
        List<OwnersItemDto> getResult = itemService.getOwnersItems(userDto.getId());

        assertThat(getResult, notNullValue());
        assertThat(getResult.getFirst().getId(), equalTo(itemDto.getId()));
        assertThat(getResult.getFirst().getName(), equalTo(itemDto.getName()));
        assertThat(getResult.getFirst().getDescription(), equalTo(itemDto.getDescription()));
    }

    @Test
    void shouldGetById() {

        ItemDto getResult = itemService.getById(userDto.getId(), itemDto.getId());
        assertThat(getResult, notNullValue());
        assertThat(getResult.getId(), equalTo(itemDto.getId()));
        assertThat(getResult.getName(), equalTo(itemDto.getName()));
        assertThat(getResult.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(getResult.getAvailable(), equalTo(itemDto.getAvailable()));
    }

    @Test
    void shouldGetSearchedItems() {
        List<ItemDto> searchedItems = itemService.getSearchedItems(userDto.getId(), "guitar");
        assertThat(searchedItems, notNullValue());
        assertThat(searchedItems.size(), equalTo(1));
        assertThat(searchedItems.getFirst().getId(), equalTo(itemDto.getId()));

        List<ItemDto> searchedItems2 = itemService.getSearchedItems(userDto.getId(), "");
        assertThat(searchedItems2, notNullValue());
        assertThat(searchedItems2.size(), equalTo(0));

        List<ItemDto> searchedItems3 = itemService.getSearchedItems(userDto.getId(), "fender");
        assertThat(searchedItems3, notNullValue());
        assertThat(searchedItems3.size(), equalTo(1));
        assertThat(searchedItems3.getFirst().getId(), equalTo(itemDto.getId()));
    }

    @Test
    void shouldCreateItem() {
        ItemDto userItem = new ItemDto(3L,"Piano", "new electric piano",
                true, LocalDateTime.of(2024,12,12,0,0), null, null, 4L);
        userItem = itemService.create(userDto.getId(), userItem);

        TypedQuery<Item> query = entityManager.createQuery("select i from Item i where i.id = :id", Item.class);
        Item qItem = query.setParameter("id", userItem.getId()).getSingleResult();
        assertThat(qItem, notNullValue());
        assertThat(userItem.getId(), equalTo(qItem.getId()));
        assertThat(userItem.getName(), equalTo(qItem.getName()));
        assertThat(userItem.getDescription(), equalTo(qItem.getDescription()));
        assertThat(userItem.getAvailable(), equalTo(qItem.getAvailable()));
    }

    @Test
    void shouldUpdateItem() {
        itemDto.setName("Stratocaster");
        itemDto.setDescription("best guitar");
        itemDto.setAvailable(true);
        itemDto = itemService.update(userDto.getId(), itemDto.getId(), itemDto);

        TypedQuery<Item> query = entityManager.createQuery("select i from Item i where i.id = :id", Item.class);
        Item qItem = query.setParameter("id", itemDto.getId()).getSingleResult();

        assertThat(qItem, notNullValue());
        assertThat(itemDto.getId(), equalTo(qItem.getId()));
        assertThat(itemDto.getName(), equalTo(qItem.getName()));
        assertThat(itemDto.getDescription(), equalTo(qItem.getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(qItem.getAvailable()));
    }

    @Test
    void shouldCreateComment() {
        BookingDto bookingDto = new BookingDto(1L, wasMinus, was, BookingStatus.WAITING, itemDto.getId(), item, booker);
        bookingDto = bookingService.create(userDto.getId(), bookingDto);
        bookingService.update(userDto.getId(), bookingDto.getId(), true);

        commentDto = itemService.createComment(userDto.getId(), itemDto.getId(), commentDto);

        TypedQuery<Comment> query = entityManager.createQuery("select c from Comment c where c.text = :text", Comment.class);
        Comment qComment = query.setParameter("text", commentDto.getText()).getSingleResult();
        assertThat(qComment, notNullValue());
        assertThat(commentDto.getId(), equalTo(qComment.getId()));
        assertThat(commentDto.getText(), equalTo(qComment.getText()));
        assertThat(commentDto.getCreated(), equalTo(qComment.getCreated()));
    }

}