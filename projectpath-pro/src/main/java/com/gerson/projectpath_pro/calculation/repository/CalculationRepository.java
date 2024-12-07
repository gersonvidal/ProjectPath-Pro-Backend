package com.gerson.projectpath_pro.calculation.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationRepository extends CrudRepository<Calculation, Long>{

    Optional<Calculation> findByProjectId(Long projectId);

}
