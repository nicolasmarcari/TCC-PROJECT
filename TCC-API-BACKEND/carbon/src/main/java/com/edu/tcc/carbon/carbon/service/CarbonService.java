package com.edu.tcc.carbon.carbon.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.edu.tcc.carbon.carbon.dto.CalculationRequestDTO;
import com.edu.tcc.carbon.carbon.dto.CalculationResponseDTO;
import com.edu.tcc.carbon.carbon.dto.dtoUser.CalculationRequestUserDTO;
import com.edu.tcc.carbon.carbon.exceptions.FuelNotFoundException;

@Service
public class CarbonService {
    CalculationRequestDTO calculationRequest = new CalculationRequestDTO();
    private final double fatorGasolina = 0.5;
    private final double fatorDiesel = 0.6;
    public CalculationResponseDTO getCarbon(CalculationRequestUserDTO request) {
        //TODO: inserir fatores corretos (Os dados reais serão passados pelo Maycon.)


        //Tranferindo os dados para o DTO
        calculationRequest.setId(UUID.randomUUID());
        calculationRequest.setVehicle(request.getVehicle());
        //0 = diesel or 1= gasoline
        calculationRequest.setTypeOfFuel(request.getTypeOfFuel());
        calculationRequest.setDistanceTraveled(request.getDistanceTraveled());
        calculationRequest.setEfficiency(request.getEfficiency());

        CalculationResponseDTO calculationResponse = new CalculationResponseDTO();

        //Consumo de combustível = Distância * Eficiência
        double fuelConsumption = calculationRequest.getDistanceTraveled() * calculationRequest.getEfficiency();
        
        double carbonFootprint = 0.0;
        switch (calculationRequest.getTypeOfFuel()) {
            case 0:
                carbonFootprint = fuelConsumption * fatorDiesel;
                break;
            case 1:
                carbonFootprint = fuelConsumption * fatorGasolina;
                break;
            default:
                throw new FuelNotFoundException();
            }
            calculationResponse.setCarbonFootprint(carbonFootprint);
            calculationResponse.setId(calculationRequest.getId());
            return calculationResponse;
    }
    public CalculationRequestDTO getAllData() {
        return calculationRequest;
    }
}
