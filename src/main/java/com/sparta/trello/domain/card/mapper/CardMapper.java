package com.sparta.trello.domain.card.mapper;

import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface CardMapper {


    @Mapping(source = "trelloColumn.id", target = "trelloColumnId")
    CardResponse toCardResponse(Card card);
    
}
