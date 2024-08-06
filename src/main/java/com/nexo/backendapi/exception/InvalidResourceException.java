package com.nexo.backendapi.exception;

import org.springframework.http.HttpStatus;

public class InvalidResourceException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final int code;

    public InvalidResourceException() {
        super("La solicitud es invalida");
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = httpStatus.value();
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

