package com.edu.tcc.carbon.carbon.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuelNotFoundException extends RuntimeException {

    public FuelNotFoundException() {
        super("Tipo de Combustível inválido.");
    }

    public FuelNotFoundException(String message) {
        super(message);
    }
}
