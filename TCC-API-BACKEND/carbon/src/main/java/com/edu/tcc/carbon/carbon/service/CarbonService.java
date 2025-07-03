package com.edu.tcc.carbon.carbon.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.edu.tcc.carbon.carbon.dto.CalculationRequestDTO;
import com.edu.tcc.carbon.carbon.dto.CalculationResponseDTO;
import com.edu.tcc.carbon.carbon.dto.dtoUser.CalculationRequestUserDTO;

@Service
public class CarbonService {
    public CalculationResponseDTO getCarbon(CalculationRequestUserDTO request) {
        //TODO: inserir fatores corretos (Os dados reais serão passados pelo Maycon.)
        final double fatorGasolina = 0.5;
        final double fatorDiesel = 0.6;

        CalculationRequestDTO calculationRequest = new CalculationRequestDTO();

        calculationRequest.setId(UUID.randomUUID());
        calculationRequest.setVehicle(request.getVehicle());
        //0 = diesel or 1= gasoline
        calculationRequest.setTypeOfFuel(request.getTypeOfFuel());
        calculationRequest.setDistanceTraveled(request.getDistanceTraveled());
        calculationRequest.setEfficiency(request.getEfficiency());

        //Lógica do aplicação:
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
                break;
        }

        CalculationResponseDTO calculationResponse = new CalculationResponseDTO(
            calculationRequest.getId(),
            carbonFootprint);
        return calculationResponse;
    }
}
