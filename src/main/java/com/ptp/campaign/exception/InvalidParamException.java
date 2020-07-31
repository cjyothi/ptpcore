package com.ptp.campaign.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidParamException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidParamException(String message) {
        super(message);
    }
}
