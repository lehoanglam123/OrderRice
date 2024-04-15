package com.bezkoder.spring.datajpa.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;

@ControllerAdvice
public class ControllerExceptionHandler {

	@Order(1)
	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDto> handleException(MethodArgumentNotValidException ex) {

		ErrorDto dto = new ErrorDto();
		dto.setStatus(400);
		dto.setMessages(ex.getFieldError().getDefaultMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);

	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> Exception(Exception ex) {

		ErrorDto dto = new ErrorDto();
		dto.setStatus(400);
		dto.setMessages(ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
	}
	@Data
	public static class ErrorDto {
		private int status;
		private String messages;
	}

}