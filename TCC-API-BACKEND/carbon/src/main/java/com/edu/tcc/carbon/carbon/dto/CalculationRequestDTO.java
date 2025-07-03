package com.edu.tcc.carbon.carbon.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculationRequestDTO {
    UUID id;
    String vehicle;
    int typeOfFuel;
    int distanceTraveled;
    double efficiency;
}
