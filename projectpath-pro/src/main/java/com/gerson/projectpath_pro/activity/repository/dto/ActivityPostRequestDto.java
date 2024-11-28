package com.gerson.projectpath_pro.activity.repository.dto;

import com.gerson.projectpath_pro.project.repository.dto.ProjectDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityPostRequestDto {

    private String name;

    private String label;

    private String predecessors;

    private Integer daysDuration;

    private ProjectDto projectDto;

}
