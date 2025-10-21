package com.edu.tcc.carbon.carbon.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculationRequestUserDTO {
    @JsonProperty(value = "veiculo", required = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotBlank(message = "O veículo deve ser preenchido.")
    String vehicle;
    @JsonProperty(value = "tipo_combustivel", required = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotBlank(message = "O tipo de combustível deve ser preenchido.")
    String typeOfFuel;
    @JsonProperty(value = "distancia_percorrida", required = true)
    @Positive(message = "A distância percorrida deve ser maior que zero.")
    @NotNull(message = "A distância deve ser preenchido.")
    int distanceTraveled;
    @JsonProperty(value = "eficiencia", required = true)
    @Positive(message = "A eficiência deve ser maior que zero.")
    @NotNull(message = "A eficiência deve ser preenchido.")
    double efficiency;
}
