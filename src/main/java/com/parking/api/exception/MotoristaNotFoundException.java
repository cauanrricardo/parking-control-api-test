package com.parking.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MotoristaNotFoundException extends RuntimeException{
    public MotoristaNotFoundException(Long id) {
        super("Motorista não encontrado com o ID: " + id);
    }
}
