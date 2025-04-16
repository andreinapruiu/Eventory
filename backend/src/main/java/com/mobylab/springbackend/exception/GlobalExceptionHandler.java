package com.mobylab.springbackend.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorObject> handleBadRequest(RuntimeException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject();

        errorObject
                .setStatusCode(HttpStatus.BAD_REQUEST.value())
                .setMessage(ex.getMessage())
                .setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InternalServerErrorException.class})
    public ResponseEntity<ErrorObject> handleInternalServerError(RuntimeException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject();

        errorObject
                .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage(ex.getMessage())
                .setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorObject> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject()
                .setStatusCode(HttpStatus.NOT_FOUND.value())
                .setMessage(ex.getMessage())
                .setTimestamp(LocalDateTime.now());
                
        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorObject> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject()
                .setStatusCode(HttpStatus.CONFLICT.value())
                .setMessage(ex.getMessage())
                .setTimestamp(LocalDateTime.now());
                
        return new ResponseEntity<>(errorObject, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorObject errorObject = new ErrorObject()
                .setStatusCode(HttpStatus.BAD_REQUEST.value())
                .setMessage("Validation error")
                .setErrors(errors)
                .setTimestamp(LocalDateTime.now());
                
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }
}
