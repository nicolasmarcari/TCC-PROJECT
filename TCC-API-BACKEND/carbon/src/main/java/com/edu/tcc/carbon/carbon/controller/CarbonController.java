package com.edu.tcc.carbon.carbon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.edu.tcc.carbon.carbon.dto.CalculationRequestUserDTO;
import com.edu.tcc.carbon.carbon.dto.CalculationResponseAllDataDTO;
import com.edu.tcc.carbon.carbon.dto.CalculationResponseDTO;
import com.edu.tcc.carbon.carbon.service.CarbonService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
@CrossOrigin()
@Validated
public class CarbonController {
    @Autowired
    private CarbonService carbonService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders headers = new HttpHeaders();
    @Value("${api.authorization.token}")
    private String authorizationToken;
    
    @PostMapping("/sendCarbon")
    public @ResponseBody ResponseEntity<CalculationResponseDTO> getMethodName(@Valid @RequestBody CalculationRequestUserDTO requestDTO) {
        headers.set("Authorization", authorizationToken);
        CalculationResponseDTO response = carbonService.getCarbon(requestDTO);
        CalculationResponseAllDataDTO allData = carbonService.getAllData(response);

        HttpEntity<CalculationResponseAllDataDTO> entityPost = new HttpEntity<>(allData, headers);
        //Realizar Post
        String url = "http://localhost:3000/saveVehicle";
        ResponseEntity<String> responsePost = restTemplate.postForEntity(
            url,
            entityPost,
            String.class
        );
        return ResponseEntity.ok().body(response);

    }
    @GetMapping("/getVehicle/{uuid}")
    public @ResponseBody ResponseEntity<String> getMethodName(@PathVariable String uuid) {
        headers.set("Authorization", authorizationToken);
        
        HttpEntity<String> entityGet = new HttpEntity<>(headers);
        //Realizar Get
        String url = "http://localhost:3000/getVehicle/" + uuid;
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entityGet,
            String.class
        );

        if (response.getStatusCode().is4xxClientError()) {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }else if(response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok().body(response.getBody());
        }else if(response.getStatusCode().is5xxServerError()) {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }else {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    
    }
}
