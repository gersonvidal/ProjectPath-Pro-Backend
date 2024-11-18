package com.gerson.projectpath_pro;

import com.gerson.projectpath_pro.project.repository.Project;

public class TestDataUtil {

    public static Project createTestProjectA() {
        return Project.builder()
                .id(1L)
                .name("ProjectPath-Pro")
                .description("A project made to automatize the project network building and critical path calculation")
                .build();
    }

    public static Project createTestProjectB() {
        return Project.builder()
                .id(2L)
                .name("FitnessTracker")
                .description("A project made to keep track of your gym weights and PRs")
                .build();
    }

    public static Project createTestProjectC() {
        return Project.builder()
                .id(3L)
                .name("Healthcare AI")
                .description("A project made to predict a possible disease based on a list of 132 symptoms")
                .build();
    }

}
