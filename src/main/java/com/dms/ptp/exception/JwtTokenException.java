package com.dms.ptp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtTokenException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public JwtTokenException(String message) {
		super(message);
	}
}
