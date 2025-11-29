package com.mendes.example.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * @deprecated Use ProblemDetail from Spring Framework 7.x (RFC 9457) instead.
 *
 * Este DTO é mantido apenas para compatibilidade retroativa.
 * Todas as novas respostas devem usar ProblemDetail de acordo com RFC 9457.
 *
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#webmvc-problem-details">Problem Details for HTTP APIs</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Deprecated(since = "1.1.0", forRemoval = true)
public class ErrorResponse {
    private Integer status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private String error;
}
