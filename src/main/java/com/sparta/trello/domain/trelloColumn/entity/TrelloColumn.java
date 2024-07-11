package com.sparta.trello.domain.trelloColumn.entity;

import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

	// card와 매핑
	@OneToMany(mappedBy = "trelloColumn", cascade = CascadeType.ALL)
	private List<Card> cards;

	public TrelloColumn updatePosition(Integer newPosition) {
		return TrelloColumn.builder()
			.id(this.id)
			.title(this.title)
			.position(newPosition)
			.build();
	}
}
