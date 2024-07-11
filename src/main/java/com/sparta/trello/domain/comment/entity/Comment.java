package com.sparta.trello.domain.comment.entity;

import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.common.entity.BaseEntity;
import com.sparta.trello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentStatus status = CommentStatus.ACTIVE;

    public void softDelete() {
        this.status = CommentStatus.DELETED;
    }
    public void update(String content) {
        this.content = content;

    }
}
