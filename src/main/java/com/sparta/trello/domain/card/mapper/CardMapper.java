package com.sparta.trello.domain.card.mapper;

import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CardMapper {
   // @Mapping(target = "id", ignore = ture)
    Card toCardEntity(CardRequest dto);

//    @Mapping(target = "columnId", source = "column.id")
//    CardResponse toCardResponseDto(Card card);
}
