package com.nexo.backendapi.exception;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ChangeSetPersister.NotFoundException {
    private final String field;
    private final HttpStatus httpStatus;
    private final int code;

    public ResourceNotFoundException(String field) {
        this.field = field;
        this.httpStatus = HttpStatus.NOT_FOUND;
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
        return "Recurso '"+ field +"' no encontrado";
    }
}
