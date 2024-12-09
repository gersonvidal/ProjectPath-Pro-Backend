package com.gerson.projectpath_pro.project.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        if (projectDto.getName() == null || projectDto.getName().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (projectDto.getUserDto() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (projectDto.getUserDto().getId() == null || projectDto.getUserDto().getId() < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Project project = projectMapper.mapFrom(projectDto);

        Project savedProject = projectService.save(project);

        return new ResponseEntity<>(projectMapper.mapTo(savedProject), HttpStatus.CREATED);
    }

    @GetMapping
    public List<ProjectDto> listProjects() {
        List<Project> projects = projectService.findAll();

        return projects.stream()
                .map(projectMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable("id") Long id) {
        Optional<Project> foundProject = projectService.findById(id);

        return foundProject.map(project -> {
            ProjectDto projectDto = projectMapper.mapTo(project);
            return new ResponseEntity<>(projectDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/user/{id}")
    public ResponseEntity<List<ProjectDto>> getProjectsByUserId(@PathVariable("id") Long userId) {
        if (userId == null || userId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Project> projects = projectService.findByUserId(userId);

        List<ProjectDto> projectDtos = projects.stream()
                .map(projectMapper::mapTo)
                .collect(Collectors.toList());

        return new ResponseEntity<>(projectDtos, HttpStatus.OK);

    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ProjectDto> fullUpdateProject(
            @PathVariable("id") Long id,
            @RequestBody ProjectDto projectDto) {

        if (!projectService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (projectDto.getName() == null || projectDto.getName().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (projectDto.getUserDto() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (projectDto.getUserDto().getId() == null || projectDto.getUserDto().getId() < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        projectDto.setId(id);
        Project project = projectMapper.mapFrom(projectDto);

        Project savedProject = projectService.save(project);

        return new ResponseEntity<>(
                projectMapper.mapTo(savedProject),
                HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<ProjectDto> partialUpdate(
            @PathVariable("id") Long id,
            @RequestBody ProjectDto projectDto) {

        if (!projectService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Project project = projectMapper.mapFrom(projectDto);
        Project updatedProject = projectService.partialUpdate(id, project);

        return new ResponseEntity<>(
                projectMapper.mapTo(updatedProject),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
        projectService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
