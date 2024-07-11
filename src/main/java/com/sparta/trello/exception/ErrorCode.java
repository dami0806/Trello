package com.sparta.trello.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// Column
	COLUMN_ALREADY_EXISTS(HttpStatus.CONFLICT, "COLUMN ALREADY EXISTS"),
	BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD NOT FOUND"),
	UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED USER");


	private final HttpStatus statusCode;
	private final String message;
}
