package com.mendes.example.shared.exception;

/**
 * Exceção lançada quando um recurso não é encontrado
 * RFC 9457: Status 404 Not Found
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construtor para a exceção ResourceNotFoundException.
     *
     * @param message Mensagem de erro a ser exibida.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Construtor para a exceção ResourceNotFoundException com causa.
     *
     * @param message Mensagem de erro a ser exibida.
     * @param cause   Causa da exceção.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
