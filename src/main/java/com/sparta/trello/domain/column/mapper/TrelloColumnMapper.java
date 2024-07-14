package com.sparta.trello.domain.column.mapper;

import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.mapper.CardMapper;
import com.sparta.trello.domain.column.dto.response.TrelloColumnResponse;
import com.sparta.trello.domain.column.dto.response.TrelloColumnResponseDto;
import com.sparta.trello.domain.column.entity.TrelloColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {CardMapper.class})

public interface TrelloColumnMapper {


    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "position", target = "position")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createAt", target = "createAt")
    @Mapping(source = "updateAt", target = "updateAt")
    @Mapping(source = "cards", target = "cards", qualifiedByName = "mapCards")
    TrelloColumnResponse toTrelloColumnResponse(TrelloColumn trelloColumn);

    @Named("mapCards")
    default List<CardResponse> mapCards(List<Card> cards) {
        CardMapper cardMapper = Mappers.getMapper(CardMapper.class);
        // order 순

        return cards.stream()
                .map(cardMapper::toCardResponse)
                .collect(Collectors.toList());
    }
}
