package com.gerson.projectpath_pro.activity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.ActivityRepository;
import com.gerson.projectpath_pro.project.service.ProjectService;
import com.gerson.projectpath_pro.utils.CustomStringComparator;
import com.gerson.projectpath_pro.utils.StringValidator;
import com.gerson.projectpath_pro.utils.UtilClass;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ActivityServiceImpl implements ActivityService {

    private ActivityRepository activityRepository;

    private ProjectService projectService;

    private final String ONLY_VALUE = "ONLY";

    private final String IN_BETWEEN = "BETWEEN";

    private final String LAST_VALUE = "LAST";

    private final String NOT_PRESENT = "NOT";

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
    public boolean predecessorsActivitiesExists(String predecessors, Long projectId) {
        if (predecessors == null) {
            return true; // if the activity has no predecessors, then it will cause no problem
        }

        String[] predecessorsValues = predecessors.split(",");

        for (String predecessorLabel : predecessorsValues) {
            if (!activityRepository.existsByLabelAndProjectId(predecessorLabel, projectId)) {
                return false; // if activity doesn't exist in database it will cause an error in the future,
                              // throw false
            }
        }

        return true;
    }

    @Override
    public void validateActivitiesAreComplete(Long projectId) {
        List<String> existingLabels = activityRepository.findAllLabelsByProjectId(projectId);

        // Geneare all expected activities according to the given range.
        List<String> expectedLabels = generateExpectedLabels(existingLabels);

        // Validate that no label is missing in the existing set.
        for (String expectedLabel : expectedLabels) {
            if (!existingLabels.contains(expectedLabel)) {
                throw new EntityNotFoundException("Missing Activity: " + expectedLabel);
            }
        }
    }

    private List<String> generateExpectedLabels(List<String> existingLabels) {
        // Obtain the last tabel in the existing labels to know how far to generate
        CustomStringComparator comparator = new CustomStringComparator();
        
        String lastLabel = existingLabels.stream()
                .max(comparator)
                .orElse("A"); // if the existingLabels list is empty A will be the last label and mandatory

        List<String> labels = new ArrayList<>();
        String currentLabel = "A";

        while (true) {
            labels.add(currentLabel);
            if (currentLabel.equals(lastLabel)) {
                break;
            }
            currentLabel = getNextLabel(currentLabel);
        }

        return labels;
    }

    private String getNextLabel(String currentLabel) {
        int length = currentLabel.length();
        char lastChar = currentLabel.charAt(0);

        if (currentLabel.chars().allMatch(c -> c == 'Z')) {
            // If all letters are Z, we add a new level (ej. Z -> ZZ -> ZZZ).
            return "A".repeat(length + 1);
        } else {
            // Incrementar las letras actuales uniformemente (.).
            // Increment the actual letters evenly (ej. A -> B, AA -> BB, etc)
            char nextChar = (char) (lastChar + 1);
            return String.valueOf(nextChar).repeat(length);
        }
    }

    @Override
    public Activity partialUpdate(Long id, Activity activity) {
        activity.setId(id);

        return activityRepository.findById(id).map(existingActivity -> {

            // Validations
            if (!StringValidator.predecessorsIsNotRecursive(activity.getPredecessors(),
                    existingActivity.getLabel())) {
                System.out.println("checo si no es recursivo");
                throw new IllegalArgumentException("Activity Label cannot be in Predecessors.");
            }

            if (!StringValidator.isLabelGreaterThanPredecessors(activity.getPredecessors(),
                    existingActivity.getLabel())) {
                System.out.println("checo si es mayor el orden");
                throw new IllegalArgumentException("Activity Label cannot be lower than Predecessor.");
            }

            if (!predecessorsActivitiesExists(
                    activity.getPredecessors(),
                    existingActivity.getProject().getId())) {
                throw new IllegalArgumentException(
                        "Predecessor does not exist for project id: " + existingActivity.getProject().getId());
            }

            // Updates
            Optional.ofNullable(activity.getName()).ifPresent(existingActivity::setName);
            // Optional.ofNullable(activity.getLabel()).ifPresent(existingActivity::setLabel);
            existingActivity.setPredecessors(activity.getPredecessors()); // Updates it with null or valid predecessors
            Optional.ofNullable(activity.getDaysDuration()).ifPresent(existingActivity::setDaysDuration);

            return activityRepository.save(existingActivity);
        }).orElseThrow(() -> new RuntimeException("Activity does not exists"));
    }

    @Override
    public void delete(Long id) {
        // 1. Obtain the activity
        Activity activity = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with id: " + id));

        // 2. Obtain its project's Id
        Long projectId = activity.getProject().getId();

        // 3. Obtain its label
        String label = activity.getLabel();

        // 4. Delete from all the predecessors that activity's label (only the
        // predecessors with the activity's projectId)
        // 4.1 Obtain the list of activities under the activity's projectId
        List<Activity> activities = getActivitiesByProjectId(projectId);

        // 4.2 For each activity check what is the case (1st: The only value, 2nd: It's
        // in between 0 and n - 2, 3rd: It's the last one, 4th: Is null, 5th:
        // Doesn't contain the label)
        for (Activity currentActivity : activities) {
            // 4.2.1 Split using the "," and turn the result into an String[]
            String predecessors = currentActivity.getPredecessors();

            // Case 4: Is null
            if (predecessors == null || predecessors.isBlank()) {
                continue;
            }

            String[] predecessorsValues = predecessors.split(",");

            // 4.2.2 Check the case and perform the algorithm
            String labelStatus = determineLabelStatus(predecessorsValues, label);

            switch (labelStatus) {
                case ONLY_VALUE:
                    currentActivity.setPredecessors(null);

                    activityRepository.save(currentActivity);
                    break;

                case IN_BETWEEN:
                    String updatedPredecessors = removeLabel(predecessorsValues, label);
                    currentActivity.setPredecessors(updatedPredecessors);

                    activityRepository.save(currentActivity);
                    break;

                case LAST_VALUE:
                    updatedPredecessors = removeLastLabel(predecessorsValues);
                    currentActivity.setPredecessors(updatedPredecessors);

                    activityRepository.save(currentActivity);
                    break;

                case NOT_PRESENT:
                    // Do nothing
                    break;
            }

        }

        // 5. Delete the activity
        activityRepository.deleteById(id);
    }

    private String determineLabelStatus(String[] predecessorsValues, String label) {
        // Case 1: The only value
        if (predecessorsValues.length == 1 && predecessorsValues[0].equals(label)) {
            return ONLY_VALUE;
        }

        // Case 3: It's the last one
        if (predecessorsValues[predecessorsValues.length - 1].equals(label)) {
            return LAST_VALUE;
        }

        // Case 2: It's in between 0 and n - 2
        int found = UtilClass.binarySearch(predecessorsValues, label, new CustomStringComparator(), 2);

        if (found >= 0) {
            return IN_BETWEEN;
        }

        // Case 5: Doesn't contain the label
        if (found == -1) {
            return NOT_PRESENT;
        }

        throw new RuntimeException("Error when determining label position");

    }

    /**
     * Removes the specified label from the predecessors array and reconstructs the
     * String.
     * 
     * @param predecessorsValues The array of predecessors
     * @param label              The label to remove
     * @return The updated predecessors string
     */
    private String removeLabel(String[] predecessorsValues, String label) {
        StringBuilder updatedPredecessors = new StringBuilder();

        for (int i = 0; i < predecessorsValues.length; i++) {
            // Skip the label to be removed
            if (predecessorsValues[i].equals(label)) {
                continue;
            }

            // Append the predecessor
            updatedPredecessors.append(predecessorsValues[i]);

            // Add a comma if it's not the last element
            if (i < predecessorsValues.length - 1) {
                updatedPredecessors.append(",");
            }
        }

        return updatedPredecessors.toString();
    }

    private String removeLastLabel(String[] predecessorsValues) {
        // Create a comma-separated string ("A,B,C,D")
        StringBuilder stringBuilder = new StringBuilder(String.join(",", predecessorsValues));

        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());

        return stringBuilder.toString();

    }

}
