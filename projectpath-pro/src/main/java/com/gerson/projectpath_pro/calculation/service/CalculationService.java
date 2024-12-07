package com.gerson.projectpath_pro.calculation.service;

import java.util.Optional;

import com.gerson.projectpath_pro.calculation.repository.Calculation;

public interface CalculationService {

    Optional<Calculation> getCalculationByProjectId(Long projectId);

    void makeAllCalculationsWhenNew(Long projectId);

    byte[] getNetworkAndCriticalPathDiagram(Long projectId);

}
