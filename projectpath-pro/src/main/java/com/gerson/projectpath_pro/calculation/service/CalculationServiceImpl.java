package com.gerson.projectpath_pro.calculation.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.ActivityRepository;
import com.gerson.projectpath_pro.calculation.repository.Calculation;
import com.gerson.projectpath_pro.calculation.repository.CalculationRepository;
import com.gerson.projectpath_pro.diagram.service.DiagramService;
import com.gerson.projectpath_pro.project.repository.Project;
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
    @Transactional
    public void makeAllCalculationsWhenNew(Long projectId) {
        createEmptyCalculation(projectId);

        List<Activity> activities = calculateForwardPass(projectId);

        // Update estimated_duration in database (this is our virtual finish node)
        int estimatedDuration = calculateEstimatedDuration(projectId, activities);

        activities = calculateBackwardPass(projectId, activities, estimatedDuration);

        activities = calculateSlack(projectId, activities);

        calculateAndSaveCriticalPath(projectId, activities);

    }

    private Calculation createEmptyCalculation(Long projectId) {
        Project project = projectService.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with project id: " + projectId));

        Calculation calculation = Calculation.builder()
                .id(null)
                .criticalPath("A") // Default value so we can insert into db
                .estimatedDuration(0) // Default value so we can insert into db
                .project(project)
                .build();

        return calculationRepository.save(calculation);
    }

    private List<Activity> calculateForwardPass(Long projectId) {
        if (!projectService.isExists(projectId)) {
            throw new EntityNotFoundException("Project with id: " + projectId + " does not exist");
        }

        List<Activity> activities = activityRepository.findByProjectId(projectId);

        // Map to store calculated times
        Map<String, Activity> activityMap = activities.stream()
                .collect(Collectors.toMap(Activity::getLabel, activity -> activity));

        // Forward pass
        for (Activity activity : activities) {
            int closeStart = 0;

            // If the activity has predecessors, we use the max closeFinish of its
            // predecessors
            if (activity.getPredecessors() != null) {
                for (String predecessorLabel : activity.getPredecessors().split(",")) {
                    Activity predecessor = activityMap.get(predecessorLabel.trim());
                    closeStart = Math.max(closeStart, predecessor.getCloseFinish());
                }
            }

            // Calculate closeFinish
            int closeFinish = closeStart + activity.getDaysDuration();

            // Update activity values
            activity.setCloseStart(closeStart);
            activity.setCloseFinish(closeFinish);
        }

        // Save changes in database
        activityRepository.saveAll(activities);

        return activities;
    }

    private int calculateEstimatedDuration(Long projectId, List<Activity> activities) {
        if (!projectService.isExists(projectId)) {
            throw new EntityNotFoundException("Project with id: " + projectId + " does not exist");
        }

        // Obtain end activities
        List<Activity> endActivities = getEndActivities(projectId, activities);

        // Calculate max closeFinish
        int estimatedDuration = endActivities.stream()
                .mapToInt(Activity::getCloseFinish)
                .max()
                .orElseThrow(() -> new EntityNotFoundException("No end activities found for project id: " + projectId));

        // Update value in Calculations table
        Calculation calculation = calculationRepository.findByProjectId(projectId)
                .orElseThrow(() -> new EntityNotFoundException("No calculation found for project id: " + projectId));

        calculation.setEstimatedDuration(estimatedDuration);

        // Save updated calculation
        calculationRepository.save(calculation);

        return estimatedDuration;
    }

    private List<Activity> calculateBackwardPass(Long projectId, List<Activity> activities, int estimatedDuration) {
        if (!projectService.isExists(projectId)) {
            throw new EntityNotFoundException("Project with id: " + projectId + " does not exist");
        }

        // Map to access quickly to activities by its label
        Map<String, Activity> activityMap = activities.stream()
                .collect(Collectors.toMap(Activity::getLabel, activity -> activity));

        // Initialize distantFinish with estimated duration (this is our finish virtual
        // node close start)
        for (Activity activity : activities) {
            activity.setDistantFinish(estimatedDuration);
        }

        // Backward Pass
        // Iterate on activities in reverse order
        for (int i = activities.size() - 1; i >= 0; i--) {
            Activity activity = activities.get(i);

            int distantFinish = activity.getDistantFinish();
            int distantStart = distantFinish - activity.getDaysDuration();

            // If activity has predecessors, modify distantFinish of predecessors with the
            // min distant start of all its successors
            if (activity.getPredecessors() != null) {
                for (String predecessorLabel : activity.getPredecessors().split(",")) {
                    Activity predecessor = activityMap.get(predecessorLabel.trim());
                    if (predecessor != null) {
                        // Adjust predecessor's distantFinish
                        predecessor.setDistantFinish(Math.min(predecessor.getDistantFinish(), distantStart));
                    }
                }
            }

            // Update calculated values
            activity.setDistantStart(distantStart);
            activity.setDistantFinish(distantFinish);
        }

        // Save changes in database
        activityRepository.saveAll(activities);

        return activities;
    }

    private List<Activity> calculateSlack(Long projectId, List<Activity> activities) {
        if (!projectService.isExists(projectId)) {
            throw new EntityNotFoundException("Project with id: " + projectId + " does not exist");
        }

        // Calculate slack for each activity
        for (Activity activity : activities) {
            int closeStart = activity.getCloseStart();
            int distantStart = activity.getDistantStart();

            // Formula: Slack = DS - CS = DF - CF
            int slack = distantStart - closeStart;

            // Update the activity with the slack value
            activity.setSlack(slack);
        }

        // Save updates in database
        activityRepository.saveAll(activities);

        return activities;
    }

    private void calculateAndSaveCriticalPath(Long projectId, List<Activity> activities) {
        if (!projectService.isExists(projectId)) {
            throw new EntityNotFoundException("Project with id: " + projectId + " does not exist");
        }

        // Filter activitiees with slack 0
        List<Activity> criticalPathActivities = activities.stream()
                .filter(activity -> activity.getSlack() == 0)
                .sorted(Comparator.comparingInt(Activity::getCloseStart)) // Order by closeStart
                .collect(Collectors.toList());

        // Build the critical path as a hyphen separated String "eg: A-C-F-H"
        String criticalPath = criticalPathActivities.stream()
                .map(Activity::getLabel)
                .collect(Collectors.joining("-"));

        // Save critical path in Calculation table
        Calculation calculation = calculationRepository.findByProjectId(projectId)
                .orElseThrow(() -> new EntityNotFoundException("No calculation found for project id: " + projectId));

        calculation.setCriticalPath(criticalPath);

        // Save changes in database
        calculationRepository.save(calculation);
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

        Calculation calculation = calculationRepository.findByProjectId(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Calculation not found with project id: " + projectId));

        String criticalPath = calculation.getCriticalPath();

        Integer estimatedDuration = calculation.getEstimatedDuration();

        String source = diagramService.generatePlantUml(activities, startActivities, endActivities, criticalPath,
                estimatedDuration);

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
