package com.gerson.projectpath_pro.diagram.service;

import java.util.List;

import com.gerson.projectpath_pro.activity.repository.Activity;

public interface DiagramService {

    String generatePlantUml(List<Activity> activities, List<Activity> startActivities,
            List<Activity> endActivities, String criticalPath, Integer estimatedDuration);

    byte[] generateDiagram(String source);

}
