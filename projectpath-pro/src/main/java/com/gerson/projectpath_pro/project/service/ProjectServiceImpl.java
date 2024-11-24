package com.gerson.projectpath_pro.project.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.gerson.projectpath_pro.project.repository.Project;
import com.gerson.projectpath_pro.project.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public List<Project> findAll() {
        return StreamSupport.stream(projectRepository
                .findAll()
                .spliterator(),
                false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return projectRepository.existsById(id);
    }

    @Override
    public Project partialUpdate(Long id, Project project) {
        project.setId(id);

        return projectRepository.findById(id).map(existingProject -> {
            Optional.ofNullable(project.getName()).ifPresent(existingProject::setName);
            Optional.ofNullable(project.getDescription()).ifPresent(existingProject::setDescription);
            // TODO: Calculations

            return projectRepository.save(existingProject);
        }).orElseThrow(() -> new RuntimeException("Project does not exists"));
    }

    @Override
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

}
