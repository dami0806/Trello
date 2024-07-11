package com.sparta.trello.domain.column.service;

import org.springframework.stereotype.Service;

import com.sparta.trello.domain.column.repository.TrelloColumnRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrelloColumnService {
	private TrelloColumnRepository columnRepository;


}
