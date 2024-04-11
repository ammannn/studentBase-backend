package com.university.mcmaster.exceptions;

import com.university.mcmaster.models.dtos.request.ApiResponse;
import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ActionNotAllowedException.class)
    public ResponseEntity<?> handleActionNotAllowedException(ActionNotAllowedException ex){
        return ResponseEntity.status(ex.getCode())
                .body(ApiResponse.builder()
                        .status(ex.getCode())
                        .msg(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(MissingRequiredParamException.class)
    public ResponseEntity<?> handleMissingRequiredParamException(MissingRequiredParamException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .msg(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .msg(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(UnAuthenticatedUserException.class)
    public ResponseEntity<?> handleUnAuthenticatedUserException(UnAuthenticatedUserException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .msg(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(InvalidParamValueException.class)
    public ResponseEntity<?> handleInvalidParamValueException(InvalidParamValueException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .msg(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(FailedToPerformOperation.class)
    public ResponseEntity<?> handleFailedToPerformOperation(FailedToPerformOperation ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .msg(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .msg(ex.getMessage())
                        .build());
    }
}
