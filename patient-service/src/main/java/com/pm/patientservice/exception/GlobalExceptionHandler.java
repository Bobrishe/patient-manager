package com.pm.patientservice.exception;

import com.pm.patientservice.dto.ResponseErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationError(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getFieldErrors().forEach(er -> errors.put(er.getField(), er.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(PatientAlreadyExistsException.class)
    public ResponseEntity<ResponseErrorDto> handleParientExistsException(PatientAlreadyExistsException ex) {
        return ResponseEntity.badRequest().body(new ResponseErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(PatientNotFountException.class)
    public ResponseEntity<ResponseErrorDto> handleParientNotFoundException(PatientNotFountException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseErrorDto(ex.getMessage()));
    }
}
