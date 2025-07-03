package com.edu.tcc.carbon.carbon.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CalculationResponseDTO {
    UUID id;
    double carbonFootprint;
}
