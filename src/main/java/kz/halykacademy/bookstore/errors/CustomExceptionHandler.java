package kz.halykacademy.bookstore.errors;

import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ClientBadRequestException.class)
    protected ResponseEntity handleBadRequest(ClientBadRequestException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ModelResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    protected ResponseEntity handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ModelResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ModelResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(value = ForbiddenException.class)
    protected ResponseEntity handleBadRequest(ForbiddenException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ModelResponseDTO(e.getMessage()));
    }
}