package com.sparta.trello.domain.board.entity;

import com.sparta.trello.domain.column.entity.TrelloColumn;
import com.sparta.trello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;

    @Column(name = "boardname", nullable = false)
    private String boardName;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private BoardStatus boardStatus;

    // Column Entity 생성 후 주석 해제
    @OneToMany(mappedBy = "board")
    private List<TrelloColumn> columnList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "boardInvitationId")
    private List<BoardInvitation> boardInvitationList = new ArrayList<>();

    public Board(String boardName, String description, BoardStatus boardStatus, User user) {
        this.boardName = boardName;
        this.description = description;
        this.boardStatus = boardStatus;
        this.user = user;
    }

    public void update(String boardName, String description) {
        this.boardName = boardName;
        this.description = description;
    }

    public void softDelete() {
        this.boardStatus = BoardStatus.DELETE;
    }
}
