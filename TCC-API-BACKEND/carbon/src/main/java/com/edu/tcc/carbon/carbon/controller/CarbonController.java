package com.edu.tcc.carbon.carbon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.edu.tcc.carbon.carbon.dto.CalculationResponseDTO;
import com.edu.tcc.carbon.carbon.dto.dtoUser.CalculationRequestUserDTO;
import com.edu.tcc.carbon.carbon.service.CarbonService;

@RestController
@RequestMapping("/api")
public class CarbonController {
    @Autowired
    private CarbonService carbonService;
    
    @PostMapping("/sendCarbon")
    public @ResponseBody CalculationResponseDTO getMethodName(@RequestBody CalculationRequestUserDTO requestDTO) {
        return carbonService.getCarbon(requestDTO);
    }
}
