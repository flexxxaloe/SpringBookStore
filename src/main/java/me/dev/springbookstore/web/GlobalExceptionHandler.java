package me.dev.springbookstore.web;

import jakarta.persistence.EntityNotFoundException;
import me.dev.springbookstore.web.exceptions.AlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception e) {
        log.error("Handle exception ", e);
        var error = new ErrorResponseDto(
                "Internal server error",
                "An unexpected error occurred",
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(error);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameAlreadyExists(AlreadyExistsException e) {
        log.warn("Resource already exists: {}", e.getMessage());
        var error = new ErrorResponseDto(
                "Conflict",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT) // 409
                .body(error);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponseDto> handleOptimisticLock(
            ObjectOptimisticLockingFailureException e) {

        log.warn("Optimistic locking conflict", e);

        var error = new ErrorResponseDto(
                "Conflict",
                "The book was modified by another request. Please try again.",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT) // 409
                .body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolation(
            DataIntegrityViolationException e) {
        /*
        при нарушении ограничений целостности данных в базе данных;
        у нас по идее может быть дата race на username/email тк они unique например
         */

        log.warn("Database integrity constraint violation", e);

        String message = "The request conflicts with existing data.";

        Throwable cause = e.getMostSpecificCause();

        if (cause.getMessage() != null) {
            String causeMessage = cause.getMessage();

            if (causeMessage.contains("uk_users_username")) {
                message = "Username already exists.";
            } else if (causeMessage.contains("uk_users_email")) {
                message = "Email already exists.";
            } else if (causeMessage.contains("uk_author_name_born_date")) {
                message = "Author with this name and birth date already exists.";
            }
        }

        var error = new ErrorResponseDto(
                "Conflict",
                message,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT) // 409
                .body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFound(EntityNotFoundException e) {
        log.warn("Entity not found: {}", e.getMessage());
        var error = new ErrorResponseDto(
                "Entity not found",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND) // 404
                .body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(AccessDeniedException e) {
        log.warn("Access denied");
        var error = new ErrorResponseDto(
                "Access denied",
                "You do not have permission to access this resource",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN) //403
                .body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthentication(AuthenticationException e) {
        log.warn("Authentication failed");
        var error = new ErrorResponseDto(
                "Unauthorized",
                "Authentication failed",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED) //401
                .body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentials(BadCredentialsException e) {
        log.warn("Authentication failed: bad credentials");
        var error = new ErrorResponseDto(
                "Unauthorized",
                "Invalid username or password",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED) //401
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException e) {
        // Spring не может провалидировать @RequestBody, который помечен @Valid.
        log.warn("Validation failed ", e);

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Request validation failed");

        var error = new ErrorResponseDto(
                "Bad Request",
                message,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST) //400
                .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException e) {

        log.warn("Illegal argument: {}", e.getMessage());
        var error = new ErrorResponseDto(
                "Illegal Argument",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST) //400
                .body(error);
    }
}
