package com.grocery.store.controller;

import com.grocery.store.exception.ErrorMessage;
import com.grocery.store.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionController {

	@ExceptionHandler(value = {NotFoundException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorMessage handleCsvException(NotFoundException ex) {
		return new ErrorMessage(0, ex.getMessage());
	}

}