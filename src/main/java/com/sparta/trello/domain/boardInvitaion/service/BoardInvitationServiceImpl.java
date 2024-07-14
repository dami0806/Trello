package com.sparta.trello.domain.boardInvitaion.service;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.boardInvitaion.entity.BoardInvitation;
import com.sparta.trello.domain.boardInvitaion.repository.BoardInvitationRepository;
import com.sparta.trello.domain.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardInvitationServiceImpl implements BoardInvitationService {
    private final BoardInvitationRepository boardInvitationRepository;

    public void inviteUserToBoard(User user, Board board, String role) {
        BoardInvitation invitation = BoardInvitation.builder()
                .board(board)
                .user(user)
                .role(role)
                .status("ACCEPTED")
                .build();

        boardInvitationRepository.save(invitation);
    }

}
