package com.masai.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ErrorDetails buildError(HttpStatus status, String message, String details, String path) {
        return new ErrorDetails(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, path, details);
    }

    // Specific known exceptions

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleProductNotFound(ProductNotFoundException ex, HttpServletRequest req) {
        log.warn("Product not found: {}", ex.getMessage());
        ErrorDetails err = buildError(HttpStatus.NOT_FOUND, ex.getMessage(), null, req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleCategoryNotFound(CategoryNotFoundException ex, HttpServletRequest req) {
        log.warn("Category not found: {}", ex.getMessage());
        ErrorDetails err = buildError(HttpStatus.NOT_FOUND, ex.getMessage(), null, req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SellerNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleSellerNotFound(SellerNotFoundException ex, HttpServletRequest req) {
        log.warn("Seller not found: {}", ex.getMessage());
        ErrorDetails err = buildError(HttpStatus.NOT_FOUND, ex.getMessage(), null, req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleCustomerNotFound(CustomerNotFoundException ex, HttpServletRequest req) {
        log.warn("Customer not found: {}", ex.getMessage());
        ErrorDetails err = buildError(HttpStatus.NOT_FOUND, ex.getMessage(), null, req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorDetails> handleLoginException(LoginException ex, HttpServletRequest req) {
        log.warn("Login failure: {}", ex.getMessage());
        ErrorDetails err = buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), null, req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomerException.class)
    public ResponseEntity<ErrorDetails> handleCustomerException(CustomerException ex, HttpServletRequest req) {
        log.warn("Customer error: {}", ex.getMessage());
        ErrorDetails err = buildError(HttpStatus.FORBIDDEN, ex.getMessage(), null, req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SellerException.class)
    public ResponseEntity<ErrorDetails> handleSellerException(SellerException ex, HttpServletRequest req) {
        log.warn("Seller error: {}", ex.getMessage());
        ErrorDetails err = buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), null, req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorDetails> handleOrderException(OrderException ex, HttpServletRequest req) {
        log.warn("Order error: {}", ex.getMessage());
        ErrorDetails err = buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), null, req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    // Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.info("Validation failed: {}", msg);
        ErrorDetails err = buildError(HttpStatus.BAD_REQUEST, "Validation Failed", msg, req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleParseError(HttpMessageNotReadableException ex, HttpServletRequest req) {
        log.info("Malformed JSON or unreadable message: {}", ex.getMessage());
        ErrorDetails err = buildError(HttpStatus.BAD_REQUEST, "Malformed JSON request", ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        log.info("Illegal argument: {}", ex.getMessage());
        ErrorDetails err = buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), null, req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetails> handleNoHandler(NoHandlerFoundException ex, HttpServletRequest req) {
        log.info("No handler found: {}", ex.getRequestURL());
        ErrorDetails err = buildError(HttpStatus.NOT_FOUND, "Endpoint not found", ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    // fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAll(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        ErrorDetails err = buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
