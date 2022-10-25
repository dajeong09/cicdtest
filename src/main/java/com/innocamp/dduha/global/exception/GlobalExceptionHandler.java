package com.innocamp.dduha.global.exception;

import com.innocamp.dduha.global.common.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.NoSuchElementException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)            //400
    protected ResponseEntity<?> handleValidationException(ValidationException e) {
        return new ResponseEntity<>(ResponseDto.fail(ErrorCode.valueOf(e.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationServiceException.class)  //401
    protected ResponseEntity<?> handleAuthorizationServiceException(AuthorizationServiceException e) {
        return new ResponseEntity<>(ResponseDto.fail(ErrorCode.valueOf(e.getMessage())), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationServiceException.class)  //403
    protected ResponseEntity<?> handleAuthenticationServiceException(AuthenticationServiceException e) {
        return new ResponseEntity<>(ResponseDto.fail(ErrorCode.valueOf(e.getMessage())), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoSuchElementException.class)         //404
    protected ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(ResponseDto.fail(ErrorCode.valueOf(e.getMessage())), HttpStatus.NOT_FOUND);
    }

}