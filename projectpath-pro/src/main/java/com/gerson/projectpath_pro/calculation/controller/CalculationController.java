package com.gerson.projectpath_pro.calculation.controller;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gerson.projectpath_pro.calculation.repository.Calculation;
import com.gerson.projectpath_pro.calculation.repository.dto.CalculationDto;
import com.gerson.projectpath_pro.calculation.service.CalculationService;
import com.gerson.projectpath_pro.mappers.Mapper;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/calculations")
@CrossOrigin(origins = "http://localhost:4200")
public class CalculationController {

    private CalculationService calculationService;

    private Mapper<Calculation, CalculationDto> calculationMapper;

    public CalculationController(CalculationService calculationService,
            Mapper<Calculation, CalculationDto> calculationMapper) {
        this.calculationService = calculationService;
        this.calculationMapper = calculationMapper;
    }

    @PostMapping("/project/{id}")
    public ResponseEntity<byte[]> createNetworkAndCriticalPathDiagram(@PathVariable("id") Long projectId) {
        byte[] pngBytes = calculationService.getNetworkAndCriticalPathDiagram(projectId);

        if (pngBytes.length == 0) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(
                pngBytes,
                headers,
                HttpStatus.OK);

    }

    @GetMapping("/project/{id}")
    public ResponseEntity<CalculationDto> getCalculationByProjectId(@PathVariable("id") Long projectId) {
        if (projectId == null || projectId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Calculation> foundCalculation = calculationService.getCalculationByProjectId(projectId);

        return foundCalculation.map(calculation -> {
            CalculationDto calculationDto = calculationMapper.mapTo(calculation);
            return new ResponseEntity<>(calculationDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
