package com.sparta.trello.domain.card.mapper;

import com.sparta.trello.domain.card.dto.CardDetailResponse;
import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.comment.dto.CommentResponse;
import com.sparta.trello.domain.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mappings({
            @Mapping(source = "trelloColumn.id", target = "trelloColumnId"),
            @Mapping(source = "manager.id", target = "manager"),
            @Mapping(source = "createAt", target = "createAt"),
            @Mapping(source = "updateAt", target = "updateAt"),
            @Mapping(target = "comments", expression = "java(mapComments(card.getComments()))")
    })
    CardDetailResponse toCardDetailResponse(Card card);


    @Mappings({
            @Mapping(source = "trelloColumn.id", target = "trelloColumnId"),
            @Mapping(source = "manager.id", target = "manager"),
            @Mapping(source = "createAt", target = "createAt"),
            @Mapping(source = "updateAt", target = "updateAt"),
    })
    CardResponse toCardResponse(Card card);

    default List<CommentResponse> mapComments(List<Comment> comments) {
        if (comments == null) {
            comments = List.of(); // null일 경우 빈 리스트로 초기화
        }
        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUser().getUsername(),
                        comment.getCreateAt(),
                        comment.getUpdateAt()))
                .collect(Collectors.toList());
    }
}