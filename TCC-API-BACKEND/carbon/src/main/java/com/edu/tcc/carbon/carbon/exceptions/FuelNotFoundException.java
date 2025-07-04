package com.edu.tcc.carbon.carbon.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuelNotFoundException extends RuntimeException {

    public FuelNotFoundException() {
        super("Type of Fuel not found.");
    }

    public FuelNotFoundException(String message) {
        super(message);
    }
}
