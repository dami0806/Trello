package com.sparta.trello.domain.column.mapper;

import com.sparta.trello.domain.column.dto.response.TrelloColumnResponse;
import com.sparta.trello.domain.column.entity.TrelloColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface TrelloColumnMapper {

//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "title", target = "title")
//    @Mapping(source = "position", target = "position")
//    @Mapping(source = "status", target = "status")
//
//    TrelloColumnResponse toTrelloColumnResponse(TrelloColumn trelloColumn);
}
