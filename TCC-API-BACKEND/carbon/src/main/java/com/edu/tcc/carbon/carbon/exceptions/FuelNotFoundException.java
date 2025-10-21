package com.edu.tcc.carbon.carbon.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FuelNotFoundException extends RuntimeException {

    public FuelNotFoundException() {
        super("Type of Fuel not found.");
    }

    public FuelNotFoundException(String message) {
        super(message);
    }
}
