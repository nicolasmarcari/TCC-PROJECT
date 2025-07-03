package com.edu.tcc.carbon.carbon.dto.dtoUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculationRequestUserDTO {
    String vehicle;
    int typeOfFuel;
    int distanceTraveled;
    double efficiency;
}
