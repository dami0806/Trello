package com.sparta.trello.domain.column.entity;

import java.util.ArrayList;
import java.util.List;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.exception.DatabaseAccessException;
import com.sparta.trello.domain.common.entity.BaseEntity;

import com.sparta.trello.domain.common.util.OrderConverter;
import jakarta.persistence.*;
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

	private int position;

	@ManyToOne
	@JoinColumn(name = "board_id")
	private Board board;

	// card와 매핑
	@OneToMany(mappedBy = "trelloColumn")
	private List<Card> cards;


	@Convert(converter = OrderConverter.class)
	@Column(name = "card_order")
	private List<Long> cardOrder = new ArrayList<>();


	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TrelloColumnStatus status = TrelloColumnStatus.ACTIVE;


	public void removeCard(Long cardId) {
		cardOrder.remove(cardId);
	}

	public void updateCardOrder(List<Long> newCardOrder) {
		this.cardOrder = newCardOrder;
	}

	public void softDelete() {
		this.status = TrelloColumnStatus.DELETED;
	}
	public void restore() {
		this.status = TrelloColumnStatus.ACTIVE;
	}
	public void updatePosition(int position) {
		this.position = position;
	}

	public void updateOrderCars(List<Card> orderedCards) {
		this.cards = orderedCards;
	}
}