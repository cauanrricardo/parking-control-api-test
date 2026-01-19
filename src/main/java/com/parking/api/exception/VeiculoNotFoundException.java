package com.parking.api.exception;

public class VeiculoNotFoundException extends  RuntimeException{

    public VeiculoNotFoundException(Long id) {
        super("Veículo não encontrado com o ID: " + id);
    }
}
