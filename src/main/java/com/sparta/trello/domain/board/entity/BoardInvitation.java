package com.sparta.trello.domain.board.entity;

import com.sparta.trello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "boardInvitation")
public class BoardInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardInvitationId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Board board;

}
