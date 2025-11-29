package com.mendes.example.shared.exception;

/**
 * Exceção lançada quando uma operação é inválida
 * RFC 9457: Status 409 Conflict
 */
public class InvalidOperationException extends RuntimeException {

    /**
     * Construtor para a exceção InvalidOperationException.
     *
     * @param message A mensagem de erro a ser exibida.
     */
    public InvalidOperationException(String message) {
        super(message);
    }

    /**
     * Construtor para a exceção InvalidOperationException com causa.
     *
     * @param message A mensagem de erro a ser exibida.
     * @param cause   A causa da exceção.
     */
    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
