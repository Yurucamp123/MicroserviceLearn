package com.lethanhbinh.commonservice.advice;

import com.lethanhbinh.commonservice.exception.InvalidTokenException;
import com.lethanhbinh.commonservice.exception.NotFoundException;
import com.lethanhbinh.commonservice.model.ErrorMessage;
import com.lethanhbinh.commonservice.model.ValidationErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorMessage("500", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException (NotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorMessage("400", ex.getMessage(), HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorMessage> handleInvalidTokenException (InvalidTokenException ex) {
        return new ResponseEntity<>(
                new ErrorMessage("401", ex.getMessage(), HttpStatus.UNAUTHORIZED),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorMessage> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return new ResponseEntity<>(new ValidationErrorMessage("400", errors, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }
}
