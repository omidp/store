package com.grocery.store.controller;

import com.grocery.store.exception.ErrorMessage;
import com.grocery.store.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionController {

	@ExceptionHandler(value = {NotFoundException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorMessage handleNotFoundException(NotFoundException ex) {
		return new ErrorMessage(0, ex.getMessage());
	}

	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public List<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		return ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(x -> new ErrorMessage(0, x.getField() + " " + x.getDefaultMessage()))
			.collect(Collectors.toList());

	}

}