package com.gerson.projectpath_pro.calculation.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gerson.projectpath_pro.calculation.repository.Calculation;
import com.gerson.projectpath_pro.calculation.repository.CalculationRepository;
import com.gerson.projectpath_pro.project.service.ProjectService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CalculationServiceImpl implements CalculationService {

    private CalculationRepository calculationRepository;

    private ProjectService projectService;

    public CalculationServiceImpl(CalculationRepository calculationRepository, ProjectService projectService) {
        this.calculationRepository = calculationRepository;
        this.projectService = projectService;
    }

    @Override
    public Optional<Calculation> getCalculationByProjectId(Long projectId) {
        if (!projectService.isExists(projectId)) {
            throw new EntityNotFoundException("Project with id " + projectId + " does not exist");
        }

        return calculationRepository.findByProjectId(projectId);

    }

}
