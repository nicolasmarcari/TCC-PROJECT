package com.edu.tcc.carbon.carbon.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculationResponseAllDataDTO {
    @JsonProperty("veiculo")
    String vehicle;
    @JsonProperty("tipo_combustivel")
    String typeOfFuel;
    @JsonProperty("distancia_percorrida")
    int distanceTraveled;
    @JsonProperty("eficiencia")
    double efficiency;
    @JsonProperty("pegada_de_carbono")
    double carbonFootprint;
    @JsonProperty("uuid")
    String id;

}
