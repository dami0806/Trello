package com.sparta.trello.domain.user.repository;

import com.sparta.trello.domain.role.entity.Role;
import com.sparta.trello.domain.user.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> getUsersNotInvitedToBoard(Long boardId);
    List<User> getUsersInvitedToBoard(Long boardId);
    Role getRoleByName(String roleName);
}
