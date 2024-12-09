package com.gerson.projectpath_pro.project.service;

import java.util.List;
import java.util.Optional;

import com.gerson.projectpath_pro.project.repository.Project;

public interface ProjectService {

    Project save(Project project);

    List<Project> findAll();

    Optional<Project> findById(Long id);

    List<Project> findByUserId(Long userId);

    boolean isExists(Long id);

    Project partialUpdate(Long id, Project project);

    void delete(Long id);

}
