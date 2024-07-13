package com.sparta.trello.domain.user.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.boardInvitaion.entity.QBoardInvitation;
import com.sparta.trello.domain.role.entity.QRole;
import com.sparta.trello.domain.role.entity.Role;
import com.sparta.trello.domain.user.entity.QUser;
import com.sparta.trello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public List<User> getUsersNotInvitedToBoard(Long boardId) {
        QUser user = QUser.user;
        QBoardInvitation invitation = QBoardInvitation.boardInvitation;

        return queryFactory.selectFrom(user)
                .where(user.id.notIn(
                        queryFactory.select(invitation.user.id)
                                .from(invitation)
                                .where(invitation.board.boardId.eq(boardId))
                ))
                .fetch();
    }

    @Override
    public List<User> getUsersInvitedToBoard(Long boardId) {
        QUser user = QUser.user;
        QBoardInvitation invitation = QBoardInvitation.boardInvitation;

        return queryFactory.selectFrom(user)
                .where(user.id.in(
                        queryFactory.select(invitation.user.id)
                                .from(invitation)
                                .where(invitation.board.boardId.eq(boardId))
                ))
                .fetch();
    }

    @Override
    public Role getRoleByName(String roleName) {
        QRole role = QRole.role;

        return queryFactory.selectFrom(role)
                .where(role.roleName.eq(roleName))
                .fetchOne();
    }
}
