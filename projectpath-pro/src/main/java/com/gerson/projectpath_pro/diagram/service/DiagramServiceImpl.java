package com.gerson.projectpath_pro.diagram.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gerson.projectpath_pro.activity.repository.Activity;

import net.sourceforge.plantuml.SourceStringReader;

@Service
public class DiagramServiceImpl implements DiagramService {

    @Override
    public String generatePlantUml(List<Activity> activities, List<Activity> startActivities,
            List<Activity> endActivities, String criticalPath, Integer estimatedDuration) {

        // Convert critical path into a set for easy verification
        Set<String> criticalPathLabels = Arrays.stream(criticalPath.split("-"))
                .collect(Collectors.toSet());

        // Use a set to track the activities to which a note has been added
        Set<String> notedActivities = new HashSet<>();

        StringBuilder plantUml = new StringBuilder("@startuml\n");

        // Note with the default values of the initial virtual node
        plantUml.append("note top\n")
                .append("Inicio\n")
                .append("IC: ").append("0").append(", TC: ")
                .append("0").append("\n")
                .append("IL: ").append("0").append(", TL: ")
                .append("0").append("\n")
                .append("Dur: ").append("0").append(", Holgura: ")
                .append("0").append("\n")
                .append("end note\n");

        // Start node to starting activities (activities where its predecessor is null)
        for (Activity activity : startActivities) {
            String label = activity.getLabel();

            plantUml.append("(*top) --> ")
                    .append("\"" + label + "\"")
                    .append("\n");

            plantUml.append("note bottom\n")
                    .append(activity.getName() + "\n")
                    .append("IC: ").append(activity.getCloseStart()).append(", TC: ")
                    .append(activity.getCloseFinish()).append("\n")
                    .append("IL: ").append(activity.getDistantStart()).append(", TL: ")
                    .append(activity.getDistantFinish()).append("\n")
                    .append("Dur: ").append(activity.getDaysDuration()).append(", Holgura: ")
                    .append(activity.getSlack()).append("\n")
                    .append("end note\n");

            // Add the unique note for this activity
            notedActivities.add(label);

        }

        // Connect activities to its predecessors
        for (Activity activity : activities) {
            String label = activity.getLabel();

            if (activity.getPredecessors() != null) {
                for (String predecessor : activity.getPredecessors().split(",")) {

                    // Verify if the activitya and its predecessor are in the critical path
                    boolean isCritical = criticalPathLabels.contains(label) && criticalPathLabels.contains(predecessor);

                    plantUml.append("\"" + predecessor + "\"")
                            .append(isCritical ? " -[#red]-> " : " --> ")
                            .append("\"" + label + "\"")
                            .append("\n");

                    // Add the unique note for this activity
                    // If the label is already in the set returns false, otherwise true
                    if (notedActivities.add(label)) {
                        plantUml.append("note bottom\n")
                                .append(activity.getName() + "\n")
                                .append("IC: ").append(activity.getCloseStart()).append(", TC: ")
                                .append(activity.getCloseFinish()).append("\n")
                                .append("IL: ").append(activity.getDistantStart()).append(", TL: ")
                                .append(activity.getDistantFinish()).append("\n")
                                .append("Dur: ").append(activity.getDaysDuration()).append(", Holgura: ")
                                .append(activity.getSlack()).append("\n")
                                .append("end note\n");
                    }
                }
            }

        }

        // Connect end activities to final node
        for (Activity activity : endActivities) {
            String label = activity.getLabel();
            boolean isCritical = criticalPathLabels.contains(label);

            plantUml.append("\"" + label + "\"")
                    .append(isCritical ? " -[#red]-> " : " --> ")
                    .append("(*)\n");

        }

        // Note with the estimated duration
        // The estimated duration = CS, CF, DS, DF
        // This is the virtual final node
        plantUml.append("note bottom\n")
                .append("Final\n")
                .append("IC: ").append(estimatedDuration).append(", TC: ")
                .append(estimatedDuration).append("\n")
                .append("IL: ").append(estimatedDuration).append(", TL: ")
                .append(estimatedDuration).append("\n")
                .append("Dur: ").append("0").append(", Holgura: ")
                .append("0").append("\n")
                .append("end note\n");

        plantUml.append("caption Ruta Crítica\n");
        plantUml.append("footer INICIO-").append(criticalPath).append("-FIN\n");

        plantUml.append("@enduml");

        return plantUml.toString();
    }

    @Override
    public byte[] generateDiagram(String source) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Generate diagram
        SourceStringReader reader = new SourceStringReader(source);

        String description;

        try {
            description = reader.outputImage(outputStream).getDescription();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }

        if (description == null) {
            return new byte[0];
        }

        // Obtain image bytes
        byte[] pngBytes = outputStream.toByteArray();

        return pngBytes;
    }

}
