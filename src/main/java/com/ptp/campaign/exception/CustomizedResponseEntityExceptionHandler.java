package com.ptp.campaign.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@SuppressWarnings({ "unchecked", "rawtypes" })
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CampaignNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException(CampaignNotFoundException ex,
            WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    
    @ExceptionHandler(InvalidParamException.class)
    public ResponseEntity<Object> handleInvalidParameterException(InvalidParamException ex) {
        ErrorResponse errorResponse = new ErrorResponse("400", "target market list cannot be null or empty");
        Error error = new Error();
        error.setError(errorResponse);
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}