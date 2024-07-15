package com.sparta.trello.domain.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.board.entity.QBoard;
import com.sparta.trello.domain.boardInvitaion.entity.QBoardInvitation;
import com.sparta.trello.domain.user.entity.QUser;
import com.sparta.trello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isBoardManager(Long boardId, User user) {
        QBoardInvitation boardInvitation = QBoardInvitation.boardInvitation;
        QUser qUser = QUser.user;

        long count = queryFactory.selectFrom(boardInvitation)
                .where(boardInvitation.board.boardId.eq(boardId)
                        .and(boardInvitation.user.eq(user))
                        .and(boardInvitation.status.eq("ACCEPTED"))
                        .and(boardInvitation.role.eq("MANAGER")))
                .fetchCount();

        return count > 0;
    }
    @Override
    public boolean isBoardMember(Long boardId, User user) {
        QBoardInvitation boardInvitation = QBoardInvitation.boardInvitation;

        // 보드 멤버인지 확인하는 쿼리
        long count = queryFactory.selectFrom(boardInvitation)
                .where(boardInvitation.board.boardId.eq(boardId)
                        .and(boardInvitation.user.eq(user))
                        .and(boardInvitation.status.eq("ACCEPTED"))
                        .and(boardInvitation.role.eq("ROLE_MEMBER")))
                .fetchCount();

        return count > 0;
    }



}
