package com.gerson.projectpath_pro.activity.service;

import java.util.List;
import java.util.Optional;

import com.gerson.projectpath_pro.activity.repository.Activity;

public interface ActivityService {

    Activity save(Activity activity);

    List<Activity> findAll();

    Optional<Activity> findById(Long id);

    boolean isExists(Long id);

    Activity partialUpdate(Long id, Activity activity);

    void delete(Long id);

}
