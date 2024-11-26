package com.gerson.projectpath_pro;

import java.time.LocalDate;

import org.hibernate.annotations.Comment;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.auth.controller.RegisterRequest;
import com.gerson.projectpath_pro.project.repository.Project;
import com.gerson.projectpath_pro.project.repository.dto.ProjectDto;
import com.gerson.projectpath_pro.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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

    public static Activity createTestActivityA(Project project) {
        return Activity.builder()
                .id(1L)
                .name("Security Investigation")
                .label("A")
                .predecessors(null)
                .days_duration(3)
                .close_start(null)
                .distant_start(null)
                .close_finish(null)
                .distant_finish(null)
                .slack(null)
                .project(project)
                .build();
    }

    public static Activity createTestActivityB(Project project) {
        return Activity.builder()
                .id(2L)
                .name("Login/Sign In Screen")
                .label("B")
                .predecessors(null)
                .days_duration(2)
                .close_start(null)
                .distant_start(null)
                .close_finish(null)
                .distant_finish(null)
                .slack(null)
                .project(project)
                .build();
    }

    public static Activity createTestActivityC(Project project) {
        return Activity.builder()
                .id(3L)
                .name("Landing Page")
                .label("C")
                .predecessors("A,B")
                .days_duration(5)
                .close_start(null)
                .distant_start(null)
                .close_finish(null)
                .distant_finish(null)
                .slack(null)
                .project(project)
                .build();
    }

    public static RegisterRequest createTestRegisterRequestA() {
        return new RegisterRequest("Pepe Le Pu", "onlypepe", "pepe@gmail.com", LocalDate.of(1996, 4, 19), "peiapso");
    }

}
