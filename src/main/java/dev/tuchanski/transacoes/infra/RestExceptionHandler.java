package dev.tuchanski.transacoes.infra;

import dev.tuchanski.transacoes.exceptions.InvalidBodyRequestException;
import dev.tuchanski.transacoes.exceptions.InvalidDataHoraException;
import dev.tuchanski.transacoes.exceptions.InvalidValorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InvalidDataHoraException.class)
    private ResponseEntity<RestErrorMessage> invalidDataHoraExceptionHandler(InvalidDataHoraException e) {
        RestErrorMessage response = new RestErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(InvalidValorException.class)
    private ResponseEntity<RestErrorMessage> invalidValorException(InvalidValorException e) {
        RestErrorMessage response = new RestErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(InvalidBodyRequestException.class)
    private ResponseEntity<RestErrorMessage> invalidBodyRequestException(InvalidBodyRequestException e) {
        RestErrorMessage response = new RestErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
