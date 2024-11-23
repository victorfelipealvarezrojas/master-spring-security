package com.cursos.api.springsecuritycourse.exception;

import com.cursos.api.springsecuritycourse.dto.ApiError;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

// Esta clase maneja las excepciones globales de la aplicación
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerGenericException(Exception exception, HttpServletRequest request){

        ApiError error = new ApiError();
        error.setMessage("Error interno en el servidor, vuelva a intentarlo");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(500);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handlerAccessDeniedException(AccessDeniedException exception, HttpServletRequest request){

        ApiError error = new ApiError();
        error.setMessage("Error: no posee los permisos necesarios para acceder a este recurso");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(500);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handlerExpiredJwtException(ExpiredJwtException exception, HttpServletRequest request){

        ApiError error = new ApiError();
        error.setMessage("Error: El Token en la solicitud ha expirado");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(500);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request){

        ApiError error = new ApiError();
        error.setMessage("Error: la petición enviada posee un formato incorrecto");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(400);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
