package com.sparta.trello.domain.board.entity;

import com.sparta.trello.domain.column.entity.TrelloColumn;
import com.sparta.trello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "board")
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

    @OneToMany(mappedBy = "board")
    private List<TrelloColumn> columnList = new ArrayList<>();

    @OneToMany(mappedBy = "boardInvitationId")
    private List<BoardInvitation> boardInvitationList = new ArrayList<>();

    public Board(String boardName, String description, BoardStatus boardStatus) {
        this.boardName = boardName;
        this.description = description;
        this.boardStatus = boardStatus;
    }

    public void update(String boardName, String description) {
        this.boardName = boardName;
        this.description = description;
    }

    public void softDelete() {
        this.boardStatus = BoardStatus.DELETE;
    }
}
