package com.gerson.projectpath_pro.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gerson.projectpath_pro.mappers.Mapper;
import com.gerson.projectpath_pro.project.repository.Project;
import com.gerson.projectpath_pro.project.repository.dto.ProjectDto;
import com.gerson.projectpath_pro.project.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private ProjectService projectService;

    private Mapper<Project, ProjectDto> projectMapper;

    public ProjectController(ProjectService projectService, Mapper<Project, ProjectDto> projectMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
        Project project = projectMapper.mapFrom(projectDto);

        Project savedProject = projectService.createProject(project);

        return new ResponseEntity<>(projectMapper.mapTo(savedProject), HttpStatus.CREATED);
    }

}
