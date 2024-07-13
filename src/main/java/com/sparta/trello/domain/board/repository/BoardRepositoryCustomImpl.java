package com.sparta.trello.domain.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.board.entity.QBoard;
import com.sparta.trello.domain.user.entity.QUser;
import com.sparta.trello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isBoardManager(Long boardId, User user) {
        QBoard board = QBoard.board;
        QUser qUser = QUser.user;

        long count = queryFactory.selectFrom(board)
                .where(board.boardId.eq(boardId)
                        .and(board.user.eq(user)))
                .fetchCount();

        return count > 0;
    }
}
