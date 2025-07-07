package com.edu.tcc.carbon.carbon.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalculationResponseDTO {
    @JsonProperty("uuid")
    UUID id;
    @JsonProperty("pegada_de_carbono")
    double carbonFootprint;
}
