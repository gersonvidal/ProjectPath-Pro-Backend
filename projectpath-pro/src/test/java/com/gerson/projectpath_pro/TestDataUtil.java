package com.gerson.projectpath_pro;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.dto.ActivityPatchRequestDto;
import com.gerson.projectpath_pro.activity.repository.dto.ActivityPostRequestDto;
import com.gerson.projectpath_pro.auth.controller.RegisterRequest;
import com.gerson.projectpath_pro.calculation.repository.Calculation;
import com.gerson.projectpath_pro.project.repository.Project;
import com.gerson.projectpath_pro.project.repository.dto.ProjectDto;
import com.gerson.projectpath_pro.user.repository.User;

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
                .daysDuration(3)
                .closeStart(null)
                .distantStart(null)
                .closeFinish(null)
                .distantFinish(null)
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
                .daysDuration(2)
                .closeStart(null)
                .distantStart(null)
                .closeFinish(null)
                .distantFinish(null)
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
                .daysDuration(5)
                .closeStart(null)
                .distantStart(null)
                .closeFinish(null)
                .distantFinish(null)
                .slack(null)
                .project(project)
                .build();
    }

    public static RegisterRequest createTestRegisterRequestA() {
        return new RegisterRequest("Pepe Le Pu", "onlypepe", "pepe@gmail.com", LocalDate.of(1996, 4, 19), "peiapso");
    }

    public static ActivityPostRequestDto createTestActivityPostRequestDtoA(ProjectDto projectDto) {
        return ActivityPostRequestDto.builder()
                .name("Security Investigation")
                .label("A")
                .predecessors(null)
                .daysDuration(3)
                .projectDto(projectDto)
                .build();
    }

    public static ActivityPatchRequestDto createTestActivityPatchRequestDtoA() {
        return ActivityPatchRequestDto.builder()
                .name("UI")
                .predecessors(null)
                .daysDuration(3)
                .build();
    }

    public static ActivityPostRequestDto createTestActivityPostRequestDtoB(ProjectDto projectDto) {
        return ActivityPostRequestDto.builder()
                .name("Login/Sign In Screen")
                .label("B")
                .predecessors(null)
                .daysDuration(2)
                .projectDto(projectDto)
                .build();
    }

    public static Calculation createTestCalculationA(Project project) {
        return Calculation.builder()
                .id(1L)
                .criticalPath("L-M-N-O-P")
                .estimatedDuration(40)
                .project(project)
                .build();
    }

    public static Calculation createTestCalculationB(Project project) {
        return Calculation.builder()
                .id(2L)
                .criticalPath("B-C-D")
                .estimatedDuration(15)
                .project(project)
                .build();
    }

    public static Calculation createTestCalculationC(Project project) {
        return Calculation.builder()
                .id(3L)
                .criticalPath("B-C-D-E-F-G-H")
                .estimatedDuration(35)
                .project(project)
                .build();
    }

}
