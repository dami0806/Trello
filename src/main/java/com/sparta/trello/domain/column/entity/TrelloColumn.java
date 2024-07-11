package com.sparta.trello.domain.column.entity;

import java.util.List;

import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.common.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TrelloColumn extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private Integer position;

	// @ManyToOne
	// @JoinColumn(name = "board_id")
	// private Board board;

	// card와 매핑
	@OneToMany(mappedBy = "trelloColumn", cascade = CascadeType.ALL)
	private List<Card> cards;

}