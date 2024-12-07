package com.gerson.projectpath_pro.calculation.repository.dto;

import com.gerson.projectpath_pro.project.repository.dto.ProjectDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalculationDto {

    private Long id;

    private String critical_path;

    private Integer estimated_duration;

    private ProjectDto projectDto;

}
