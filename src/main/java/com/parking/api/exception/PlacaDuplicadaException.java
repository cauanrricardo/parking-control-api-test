package com.parking.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PlacaDuplicadaException extends RuntimeException{
    public PlacaDuplicadaException(String placa) {
        super("Ja existe um veiculo cadastrado com esta placa: " + placa);
    }
}
