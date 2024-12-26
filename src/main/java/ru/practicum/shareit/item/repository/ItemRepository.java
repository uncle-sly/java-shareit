package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = """
            SELECT * FROM items i
                    WHERE (LOWER(i.name) LIKE CONCAT('%', :text, '%') OR LOWER(i.description) LIKE CONCAT('%', :text, '%'))
                      AND i.available = true;
            """, nativeQuery = true)
    List<Item> findAvailableByNameOrDescriptionContainingText(@Param("text") String text);

    List<Item> findByOwner(User user);

    List<Long> findAllByOwner_Id(Long ownerId);
}
