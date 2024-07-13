package com.sparta.trello.domain.comment.mapper;

import com.sparta.trello.domain.comment.dto.CommentResponse;
import com.sparta.trello.domain.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "createAt", target = "createAt")
    @Mapping(source = "updateAt", target = "updateAt")
    CommentResponse toCommentResponse(Comment comment);

    List<CommentResponse> toCommentResponseList(List<Comment> comments);

}