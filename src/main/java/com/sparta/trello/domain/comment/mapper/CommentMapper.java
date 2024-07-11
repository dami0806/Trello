package com.sparta.trello.domain.comment.mapper;

import com.sparta.trello.domain.comment.dto.CommentResponse;
import com.sparta.trello.domain.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "user.name", target = "username")
    CommentResponse toCommentResponse(Comment comment);
}