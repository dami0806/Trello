package com.sparta.trello.domain.card.entity;

import com.sparta.trello.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private String manager;

//    @ManyToOne
//    @JoinColumn(name = "column_id",nullable = false)
//    private Column colum;

    private int position;

    public void updatePosition(int position) {
        this.position = position;
    }
//    public updateColumn(Column column) {
//        this.column = column;
//    }
}
