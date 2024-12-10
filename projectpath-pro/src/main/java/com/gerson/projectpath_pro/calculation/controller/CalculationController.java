package com.gerson.projectpath_pro.calculation.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
public class CalculationController {

    private CalculationService calculationService;

    private Mapper<Calculation, CalculationDto> calculationMapper;

    public CalculationController(CalculationService calculationService,
            Mapper<Calculation, CalculationDto> calculationMapper) {
        this.calculationService = calculationService;
        this.calculationMapper = calculationMapper;
    }

    @PostMapping("/project/{id}")
    public ResponseEntity<String> createAndGetNetworkAndCriticalPathDiagram(@PathVariable("id") Long projectId) {
        if (projectId == null || projectId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            calculationService.makeAllCalculationsWhenNew(projectId);

            String base64Image = calculationService.getNetworkAndCriticalPathDiagram(projectId);

            if (base64Image == null || base64Image.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(
                    base64Image,
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/project/diagram/{id}")
    public ResponseEntity<String> updateAndGetNetworkAndCriticalPathDiagram(@PathVariable("id") Long projectId) {
        if (projectId == null || projectId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            calculationService.makeAllCalculationsWhenAlreadyExists(projectId);

            String base64Image = calculationService.getNetworkAndCriticalPathDiagram(projectId);

            if (base64Image == null || base64Image.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(
                    base64Image,
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

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

    @GetMapping("/project/diagram/{id}")
    public ResponseEntity<String> getNetworkAndCriticalPathDiagram(@PathVariable("id") Long projectId) {
        if (projectId == null || projectId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String base64Image = calculationService.getNetworkAndCriticalPathDiagram(projectId);

        if (base64Image == null || base64Image.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(base64Image, HttpStatus.OK);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
