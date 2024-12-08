package com.gerson.projectpath_pro.project.repository.dto;

import com.gerson.projectpath_pro.user.repository.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDto {

    private Long id;

    private String name;

    private String description;

    private UserDto userDto;

}
