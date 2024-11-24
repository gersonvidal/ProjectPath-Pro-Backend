package com.gerson.projectpath_pro;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.gerson.projectpath_pro.auth.controller.RegisterRequest;
import com.gerson.projectpath_pro.project.repository.Project;
import com.gerson.projectpath_pro.project.repository.dto.ProjectDto;
import com.gerson.projectpath_pro.user.User;

public class TestDataUtil {

    public static User createTestUserA(PasswordEncoder passwordEncoder) {
        return User.builder()
                .fullName("Pepe Le Pu")
                .username("onlypepe")
                .email("pepe@gmail.com")
                .dateOfBirth(LocalDate.of(1996, 4, 19))
                .password(passwordEncoder.encode("peiapso"))
                .build();
    }

    public static Project createTestProjectA() {
        return Project.builder()
                .id(1L)
                .name("ProjectPath-Pro")
                .description("A project made to automatize the project network building and critical path calculation")
                .build();
    }

    public static ProjectDto createTestProjectDtoA() {
        return ProjectDto.builder()
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

    public static ProjectDto createTestProjectDtoB() {
        return ProjectDto.builder()
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

    public static RegisterRequest createTestRegisterRequestA() {
        return new RegisterRequest("Pepe Le Pu", "onlypepe", "pepe@gmail.com", LocalDate.of(1996, 4, 19), "peiapso");
    }

}
