package com.mendes.example.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global Exception Handler implementando RFC 9457 - Problem Details for HTTP APIs
 *
 * O RFC 9457 define um formato padrão para representar problemas em APIs HTTP.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String PROBLEM_BASE_URL = "https://api.example.com/problems";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String ERROR_CODE_KEY = "errorCode";

    /**
     * Trata IllegalArgumentException (validações de negócio)
     * RFC 9457: 400 Bad Request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {

        log.warn("Validation error: {}", ex.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setType(URI.create(PROBLEM_BASE_URL + "/invalid-request"));
        problem.setTitle("Invalid Request");
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(getRequestPath(request)));

        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put(TIMESTAMP_KEY, LocalDateTime.now());
        properties.put(ERROR_CODE_KEY, "VALIDATION_ERROR");

        properties.forEach(problem::setProperty);

        return new ResponseEntity<>(problem, status);
    }

    /**
     * Trata ResourceNotFoundException
     * RFC 9457: 404 Not Found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {

        log.warn("Resource not found: {}", ex.getMessage());

        HttpStatus status = HttpStatus.NOT_FOUND;

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setType(URI.create(PROBLEM_BASE_URL + "/resource-not-found"));
        problem.setTitle("Resource Not Found");
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(getRequestPath(request)));

        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put(TIMESTAMP_KEY, LocalDateTime.now());
        properties.put(ERROR_CODE_KEY, "RESOURCE_NOT_FOUND");

        properties.forEach(problem::setProperty);

        return new ResponseEntity<>(problem, status);
    }

    /**
     * Trata InvalidOperationException
     * RFC 9457: 409 Conflict
     */
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ProblemDetail> handleInvalidOperationException(
            InvalidOperationException ex,
            WebRequest request) {

        log.warn("Invalid operation: {}", ex.getMessage());

        HttpStatus status = HttpStatus.CONFLICT;

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setType(URI.create(PROBLEM_BASE_URL + "/invalid-operation"));
        problem.setTitle("Invalid Operation");
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(getRequestPath(request)));

        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put(TIMESTAMP_KEY, LocalDateTime.now());
        properties.put(ERROR_CODE_KEY, "INVALID_OPERATION");

        properties.forEach(problem::setProperty);

        return new ResponseEntity<>(problem, status);
    }

    /**
     * Trata todas as outras exceções
     * RFC 9457: 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(
            Exception ex,
            WebRequest request) {

        log.error("Unexpected error: ", ex);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setType(URI.create(PROBLEM_BASE_URL + "/internal-server-error"));
        problem.setTitle("Internal Server Error");
        problem.setDetail("An unexpected error occurred: " + ex.getMessage());
        problem.setInstance(URI.create(getRequestPath(request)));

        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put(TIMESTAMP_KEY, LocalDateTime.now());
        properties.put(ERROR_CODE_KEY, "INTERNAL_SERVER_ERROR");

        properties.forEach(problem::setProperty);

        return new ResponseEntity<>(problem, status);
    }

    /**
     * Extrai o caminho da requisição
     *
     * @param request a requisição web
     * @return o caminho da requisição
     */
    private String getRequestPath(WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return path.isEmpty() ? "/" : path;
    }
}
