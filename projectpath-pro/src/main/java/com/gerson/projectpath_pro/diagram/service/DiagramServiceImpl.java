package com.gerson.projectpath_pro.diagram.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gerson.projectpath_pro.activity.repository.Activity;

import net.sourceforge.plantuml.SourceStringReader;

@Service
public class DiagramServiceImpl implements DiagramService {

    @Override
    public String generatePlantUml(List<Activity> activities, List<Activity> startActivities,
            List<Activity> endActivities) {
        StringBuilder plantUml = new StringBuilder("@startuml\n");

        // Start node to starting activities (activities where its predecessor is null)
        for (Activity activity : startActivities) {
            plantUml.append("(*top) --> ")
                    .append("\"" + activity.getLabel() + "\"")
                    .append("\n");
        }

        // Connect activities to its predecessors
        for (Activity activity : activities) {
            if (activity.getPredecessors() != null) {
                for (String predecessor : activity.getPredecessors().split(",")) {
                    plantUml.append("\"" + predecessor + "\"")
                            .append(" --> ")
                            .append("\"" + activity.getLabel() + "\"")
                            .append("\n");
                }
            }
        }

        // Connect end activities to final node
        for (Activity activity : endActivities) {
            plantUml.append("\"" + activity.getLabel() + "\"")
                    .append(" --> (*)\n");
        }

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
