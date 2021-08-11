package com.myapp.spring.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

	public ResourceNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public ResourceNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	
}
