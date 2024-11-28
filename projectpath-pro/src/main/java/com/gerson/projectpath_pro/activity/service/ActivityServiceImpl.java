package com.gerson.projectpath_pro.activity.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.ActivityRepository;
import com.gerson.projectpath_pro.project.service.ProjectService;

@Service
public class ActivityServiceImpl implements ActivityService {

    private ActivityRepository activityRepository;

    private ProjectService projectService;

    public ActivityServiceImpl(ActivityRepository activityRepository, ProjectService projectService) {
        this.activityRepository = activityRepository;
        this.projectService = projectService;
    }

    @Override
    public Activity save(Activity activity) {
        Long projectId = activity.getProject().getId();

        if (!projectService.isExists(projectId)) {
            throw new IllegalArgumentException("Project with id " + projectId + " does not exist");
        }

        return activityRepository.save(activity);
    }

    @Override
    public List<Activity> findAll() {
        return StreamSupport.stream(activityRepository
                .findAll()
                .spliterator(),
                false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Activity> findById(Long id) {
        return activityRepository.findById(id);
    }

    @Override
    public List<Activity> getActivitiesByProjectId(Long projectId) {
        if (!projectService.isExists(projectId)) {
            throw new IllegalArgumentException("Project with id " + projectId + " does not exist");
        }

        return activityRepository.findByProjectId(projectId);
    }

    @Override
    public boolean isExists(Long id) {
        return activityRepository.existsById(id);
    }

    @Override
    public Activity partialUpdate(Long id, Activity activity) {
        activity.setId(id);

        return activityRepository.findById(id).map(existingActivity -> {
            Optional.ofNullable(activity.getName()).ifPresent(existingActivity::setName);
            Optional.ofNullable(activity.getLabel()).ifPresent(existingActivity::setLabel);
            existingActivity.setPredecessors(activity.getPredecessors()); // Updates it with null or valid predecessors
            Optional.ofNullable(activity.getDaysDuration()).ifPresent(existingActivity::setDaysDuration);

            return activityRepository.save(existingActivity);
        }).orElseThrow(() -> new RuntimeException("Activity does not exists"));
    }

    // TODO: Make a method for editing predecessors (private String predecessors)

    @Override
    public void delete(Long id) {
        activityRepository.deleteById(id);
    }

}
