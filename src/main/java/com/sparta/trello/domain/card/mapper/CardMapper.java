package com.sparta.trello.domain.card.mapper;

import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.comment.dto.CommentResponse;
import com.sparta.trello.domain.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mappings({
            @Mapping(source = "trelloColumn.id", target = "trelloColumnId"),
            @Mapping(source = "manager.name", target = "manager"),
            @Mapping(source = "createAt", target = "createAt"),
            @Mapping(source = "updateAt", target = "updateAt"),
            @Mapping(target = "comments", expression = "java(mapComments(card.getComments()))")
    })
    CardResponse toCardResponse(Card card);

    List<CardResponse> toCardResponseList(List<Card> cards);

    default List<CommentResponse> mapComments(List<Comment> comments) {

        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUser().getName(),
                        comment.getCreateAt(),
                        comment.getUpdateAt()))
                .collect(java.util.stream.Collectors.toList());
    }
}
