package com.gerson.projectpath_pro.activity.service;

import java.util.List;
import java.util.Optional;

import com.gerson.projectpath_pro.activity.repository.Activity;

public interface ActivityService {

    Activity save(Activity activity);

    List<Activity> findAll();

    Optional<Activity> findById(Long id);

    List<Activity> getActivitiesByProjectId(Long projectId);

    boolean isExists(Long id);

    boolean predecessorsActivitiesExists(String predecessors, Long projectId);

    void validateActivitiesAreComplete(Long projectId);

    Activity partialUpdate(Long id, Activity activity);

    void delete(Long id);

}
