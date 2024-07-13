package com.sparta.trello.domain.boardInvitaion.repository;

import com.sparta.trello.domain.boardInvitaion.entity.BoardInvitation;
import com.sparta.trello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardInvitationRepository extends JpaRepository<BoardInvitation, Long> {
    @Query("SELECT bi.user FROM BoardInvitation bi WHERE bi.board.boardId = :boardId")

    List<User> findUsersByBoardId(Long boardId);
}
