package com.nexo.backendapi.rest;

import com.nexo.backendapi.exception.InvalidResourceException;
import com.nexo.backendapi.exception.ResourceAlreadyExistsException;
import com.nexo.backendapi.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({
            ResourceNotFoundException.class,
            ResourceAlreadyExistsException.class,
            InvalidResourceException.class
    })
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        int statusCode;
        if (e instanceof ResourceNotFoundException) {
            statusCode = ((ResourceNotFoundException) e).getCode();
        } else if (e instanceof ResourceAlreadyExistsException) {
            statusCode = ((ResourceAlreadyExistsException) e).getCode();
        } else if (e instanceof InvalidResourceException) {
            statusCode = ((InvalidResourceException) e).getCode();
        } else {
            statusCode = 500;
        }

        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), statusCode);
        return ResponseEntity.status(statusCode).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Map<String, List<String>>>> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult objBindingResult = e.getBindingResult();
        Map<String, Map<String, List<String>>> errorMap = new HashMap<>();
        Map<String, List<String>> errors = new HashMap<>();

        for (FieldError error : objBindingResult.getFieldErrors()) {
            String key = error.getField();
            String value = error.getDefaultMessage();
            List<String> values = new ArrayList<>();

            if (errors.containsKey(key)) {
                values = errors.get(key);
            }
            values.add(value);
            errors.put(key, values);
        }
        errorMap.put("Errores", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }
}
