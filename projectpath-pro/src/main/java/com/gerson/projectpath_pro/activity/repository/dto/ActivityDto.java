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
public class ActivityDto {

    private Long id;

    private String name;

    private String label;

    private String predecessors;

    private int daysDuration;

    private Integer closeStart;

    private Integer distantStart;

    private Integer closeFinish;

    private Integer distantFinish;

    private Integer slack;

    private ProjectDto projectDto;

}
