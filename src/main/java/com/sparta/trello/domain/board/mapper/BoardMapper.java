package com.sparta.trello.domain.board.mapper;

import com.sparta.trello.domain.board.dto.response.BoardResponse;
import com.sparta.trello.domain.board.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    @Mapping(source = "boardId", target = "boardId")
    @Mapping(source = "boardName", target = "boardName")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "boardStatus", target = "boardStatus")
    BoardResponse toBoardResponse(Board board);
}
