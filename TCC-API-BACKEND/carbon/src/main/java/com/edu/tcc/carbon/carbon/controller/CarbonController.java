package com.edu.tcc.carbon.carbon.controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.edu.tcc.carbon.carbon.dto.CalculationResponseAllDataDTO;
import com.edu.tcc.carbon.carbon.dto.CalculationResponseDTO;
import com.edu.tcc.carbon.carbon.dto.dtoUser.CalculationRequestUserDTO;
import com.edu.tcc.carbon.carbon.service.CarbonService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class CarbonController {
    @Autowired
    private CarbonService carbonService;
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/sendCarbon")
    public @ResponseBody ResponseEntity<CalculationResponseDTO> getMethodName(@RequestBody CalculationRequestUserDTO requestDTO) {
        
        //Incluir URL para fazer o post
        CalculationResponseDTO response = carbonService.getCarbon(requestDTO);
        String url = "http://localhost:3000/saveVehicle";
        CalculationResponseAllDataDTO allData = carbonService.getAllData(response);
        restTemplate.postForObject(url,allData,String.class);
        
        return ResponseEntity.ok().body(carbonService.getCarbon(requestDTO));
    }
}
