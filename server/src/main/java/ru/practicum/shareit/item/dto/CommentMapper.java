package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "comment.author.name", target = "authorName")
    CommentDto toDto(Comment comment);

    List<CommentDto> toDtos(List<Comment> comments);

    @Mapping(source = "commentDto.authorName", target = "author.name")
    Comment fromDto(CommentDto commentDto);

}
