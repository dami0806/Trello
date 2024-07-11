package com.sparta.trello.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException{
	private final HttpStatus statusCode;
	private final String message;

	public BusinessException(ErrorCode errorCode) {
		this.statusCode = errorCode.getStatusCode();
		this.message = errorCode.getMessage();
	}
}
