package com.sparta.trello.domain.board.entity;

import com.sparta.trello.domain.column.entity.TrelloColumn;
import com.sparta.trello.domain.common.entity.BaseEntity;
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
public class Board extends BaseEntity {

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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