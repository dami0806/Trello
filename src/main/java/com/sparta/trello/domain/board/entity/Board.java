package com.sparta.trello.domain.board.entity;

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

    // Column Entity 생성 후 주석 해제
//    @OneToMany(mappedBy = "column")
//    private List<Column> columnList = new ArrayList<>();

    @OneToMany(mappedBy = "boardInvitationId")
    private List<BoardInvitation> boardInvitationList = new ArrayList<>();

    public Board(String boardName, String description, BoardStatus boardStatus) {
        this.boardName = boardName;
        this.description = description;
        this.boardStatus = boardStatus;
    }
}
