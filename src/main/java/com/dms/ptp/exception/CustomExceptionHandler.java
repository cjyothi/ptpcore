package com.dms.ptp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@SuppressWarnings({ "unchecked", "rawtypes" })
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse("500", ex.getLocalizedMessage());
		Error error = new Error();
		error.setError(errorResponse);
		return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse("4041", "Record Not Found");
		Error error = new Error();
		error.setError(errorResponse);
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNameExistsException.class)
	public ResponseEntity<Object> handleUserNameExistsException(UserNameExistsException ex) {
		ErrorResponse errorResponse = new ErrorResponse("4001", "Email address already exists");
		Error error = new Error();
		error.setError(errorResponse);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<Object> handleInvalidPasswordException(InvalidPasswordException ex) {
		ErrorResponse errorResponse = new ErrorResponse("4002", "Invalid password format");
		Error error = new Error();
		error.setError(errorResponse);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidParameterException.class)
	public ResponseEntity<Object> handleInvalidParameterException(InvalidParameterException ex) {
		ErrorResponse errorResponse = new ErrorResponse("4003", "Invalid phone number format");
		Error error = new Error();
		error.setError(errorResponse);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidLoginCredentialsException.class)
	public ResponseEntity<Object> handleInvalidLoginCredentialsException(InvalidLoginCredentialsException ex) {
		ErrorResponse errorResponse = new ErrorResponse("4004", "Invalid username or password");
		Error error = new Error();
		error.setError(errorResponse);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}
}
