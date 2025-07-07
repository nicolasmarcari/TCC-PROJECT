package com.edu.tcc.carbon.carbon.handlers;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.edu.tcc.carbon.carbon.exceptions.FuelNotFoundException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
    
    @ExceptionHandler(FuelNotFoundException.class)
    public ResponseEntity<String> fuelNotFoundHandler(FuelNotFoundException exception) {
        return ResponseEntity.status(Response.SC_BAD_REQUEST).body("Tipo de Combustível inválido.");
    }
}
