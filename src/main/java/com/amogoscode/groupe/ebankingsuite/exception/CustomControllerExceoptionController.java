package com.amogoscode.groupe.ebankingsuite.exception;

import com.amogoscode.groupe.ebankingsuite.universal.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomControllerExceoptionController {
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
        System.out.println("yee");
        ApiResponse exceptionMessage = new ApiResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.REQUEST_TIMEOUT);
    }
}
