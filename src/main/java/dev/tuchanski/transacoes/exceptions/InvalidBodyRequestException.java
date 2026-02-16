package dev.tuchanski.transacoes.exceptions;

public class InvalidBodyRequestException extends RuntimeException {
    public InvalidBodyRequestException(String message) {
        super(message);
    }
}
