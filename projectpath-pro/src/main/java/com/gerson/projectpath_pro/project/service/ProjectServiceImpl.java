package com.gerson.projectpath_pro.project.service;

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
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

}
