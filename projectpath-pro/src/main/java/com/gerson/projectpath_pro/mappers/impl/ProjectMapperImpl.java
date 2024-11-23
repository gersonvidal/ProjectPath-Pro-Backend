package com.gerson.projectpath_pro.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.gerson.projectpath_pro.mappers.Mapper;
import com.gerson.projectpath_pro.project.repository.Project;
import com.gerson.projectpath_pro.project.repository.dto.ProjectDto;

@Component
public class ProjectMapperImpl implements Mapper<Project, ProjectDto> {

    private ModelMapper modelMapper;

    public ProjectMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ProjectDto mapTo(Project project) {
        return modelMapper.map(project, ProjectDto.class);
    }

    @Override
    public Project mapFrom(ProjectDto projectDto) {
        return modelMapper.map(projectDto, Project.class);
    }

}
