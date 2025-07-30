package org.example.myspringapp.controlleradvice;

import org.example.myspringapp.models.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.authentication.BadCredentialsException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice
public class MySpringAppRestControllerAdvice {

//    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Error> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new Error("Incorrect username or password", HttpStatus.UNAUTHORIZED.value()));
    }

//    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Error> handleExpiredJwt(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new Error("JWT token has expired", HttpStatus.UNAUTHORIZED.value()));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Error> handleGenericException(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new Error("Internal server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
//    }
}
