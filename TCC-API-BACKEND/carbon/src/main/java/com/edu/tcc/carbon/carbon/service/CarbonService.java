package com.edu.tcc.carbon.carbon.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.edu.tcc.carbon.carbon.dto.CalculationRequestDTO;
import com.edu.tcc.carbon.carbon.dto.CalculationResponseAllDataDTO;
import com.edu.tcc.carbon.carbon.dto.CalculationResponseDTO;
import com.edu.tcc.carbon.carbon.dto.dtoUser.CalculationRequestUserDTO;
import com.edu.tcc.carbon.carbon.exceptions.FuelNotFoundException;

@Service
public class CarbonService {
    CalculationRequestDTO calculationRequest = new CalculationRequestDTO();
    CalculationResponseDTO calculationResponse = new CalculationResponseDTO();
    UUID id = UUID.randomUUID();

    private final double fatorGasolina = 2.28;
    private final double fatorDiesel = 2.6;
    public CalculationResponseDTO getCarbon(CalculationRequestUserDTO request) {
        //TODO: inserir fatores corretos (Os dados reais serão passados pelo Maycon.)


        //Tranferindo os dados para o DTO
        calculationRequest.setId(id);
        calculationRequest.setVehicle(request.getVehicle());
        //0 = diesel or 1= gasoline
        calculationRequest.setTypeOfFuel(request.getTypeOfFuel());
        calculationRequest.setDistanceTraveled(request.getDistanceTraveled());
        calculationRequest.setEfficiency(request.getEfficiency());

        //Consumo de combustível = Distância * Eficiência
        double fuelConsumption = calculationRequest.getDistanceTraveled() / calculationRequest.getEfficiency();
        
        double carbonFootprint = 0.0;
        switch (calculationRequest.getTypeOfFuel().strip().toLowerCase()) {
            case "diesel":
                carbonFootprint = fuelConsumption * fatorDiesel;
                break;
            case "gasolina":
                carbonFootprint = fuelConsumption * fatorGasolina;
                break;
            default:
                throw new FuelNotFoundException();
            }
        
        calculationResponse.setCarbonFootprint(carbonFootprint);
        calculationResponse.setId(id);
        return calculationResponse;
    }

    public CalculationResponseAllDataDTO getAllData(CalculationResponseDTO calculationResponse) {
        CalculationResponseAllDataDTO calculationRequestAllData = new CalculationResponseAllDataDTO();

        calculationRequestAllData.setVehicle(calculationRequest.getVehicle());
        calculationRequestAllData.setTypeOfFuel(calculationRequest.getTypeOfFuel());
        calculationRequestAllData.setDistanceTraveled(calculationRequest.getDistanceTraveled());
        calculationRequestAllData.setEfficiency(calculationRequest.getEfficiency());
        calculationRequestAllData.setCarbonFootprint(calculationResponse.getCarbonFootprint());
        calculationRequestAllData.setId(id.toString());

        return calculationRequestAllData;
    }
}
