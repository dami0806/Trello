package com.sparta.trello.domain.card.entity;

import com.sparta.trello.domain.comment.entity.Comment;
import com.sparta.trello.domain.common.entity.BaseEntity;
import com.sparta.trello.domain.user.entity.User;
import jakarta.persistence.*;
import com.sparta.trello.domain.column.entity.TrelloColumn;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "card")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User manager; // 카드 생성자

    @ManyToOne
    @JoinColumn(name = "trelloColumn_id",nullable = false)
    private TrelloColumn trelloColumn;

    private long position;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status = CardStatus.ACTIVE;

    // 카드 위치 변경
    public void updatePosition(int position) {
        this.position = position;
    }

    // 카드 컬럼 변경
    public void updateColumn(TrelloColumn trelloColumn) {
        this.trelloColumn = trelloColumn;
    }

    public void softDelete() {
        this.status = CardStatus.DELETED;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
