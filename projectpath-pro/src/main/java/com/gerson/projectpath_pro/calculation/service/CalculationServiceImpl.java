package com.gerson.projectpath_pro.calculation.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.ActivityRepository;
import com.gerson.projectpath_pro.calculation.repository.Calculation;
import com.gerson.projectpath_pro.calculation.repository.CalculationRepository;
import com.gerson.projectpath_pro.diagram.service.DiagramService;
import com.gerson.projectpath_pro.project.service.ProjectService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CalculationServiceImpl implements CalculationService {

    private CalculationRepository calculationRepository;

    private ActivityRepository activityRepository;

    private ProjectService projectService;

    private DiagramService diagramService;

    public CalculationServiceImpl(CalculationRepository calculationRepository, ActivityRepository activityRepository,
            ProjectService projectService, DiagramService diagramService) {
        this.calculationRepository = calculationRepository;
        this.activityRepository = activityRepository;
        this.projectService = projectService;
        this.diagramService = diagramService;
    }

    @Override
    public Optional<Calculation> getCalculationByProjectId(Long projectId) {
        if (!projectService.isExists(projectId)) {
            throw new EntityNotFoundException("Project with id " + projectId + " does not exist");
        }

        return calculationRepository.findByProjectId(projectId);

    }

    @Override
    public byte[] getNetworkAndCriticalPathDiagram(Long projectId) {
        if (!projectService.isExists(projectId)) {
            throw new EntityNotFoundException("Project with id: " + projectId + " does not exist");
        }

        List<Activity> activities = activityRepository.findByProjectId(projectId);

        List<Activity> startActivities = getStartActivities(projectId, activities);

        // Find last activities (the ones thar are not predecessors of any activity)
        List<Activity> endActivities = getEndActivities(projectId, activities);

        String source = diagramService.generatePlantUml(activities, startActivities, endActivities);

        byte[] pngBytes = diagramService.generateDiagram(source);

        return pngBytes;
    }

    private List<Activity> getStartActivities(Long projectId, List<Activity> activities) {
        if (activities.isEmpty()) {
            throw new EntityNotFoundException("No activities found for project id: " + projectId);
        }

        List<Activity> startActivities = activities.stream()
                .filter(activity -> activity.getPredecessors() == null)
                .collect(Collectors.toList());

        return startActivities;
    }

    private List<Activity> getEndActivities(Long projectId, List<Activity> activities) {
        if (activities.isEmpty()) {
            throw new EntityNotFoundException("No activities found for project id: " + projectId);
        }

        // This set represents all activities that precede another activity
        Set<String> allPredecessors = activities.stream()
                .flatMap(activity -> activity.getPredecessors() != null
                        ? Arrays.stream(activity.getPredecessors().split(","))
                        : Stream.empty())
                .collect(Collectors.toSet());

        List<Activity> endActivities = activities.stream()
                .filter(activity -> !allPredecessors.contains(activity.getLabel()))
                .collect(Collectors.toList());

        return endActivities;
    }

}
