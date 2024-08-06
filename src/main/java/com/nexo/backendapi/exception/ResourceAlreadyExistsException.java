package com.nexo.backendapi.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends RuntimeException {

    private final String field;
    private final HttpStatus httpStatus;
    private final int code;

    public ResourceAlreadyExistsException(String field) {
        this.field = field;
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = httpStatus.value();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return "El recurso '" + field + "' que desea crear ya existe";
    }
}
