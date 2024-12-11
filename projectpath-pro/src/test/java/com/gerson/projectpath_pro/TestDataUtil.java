package com.gerson.projectpath_pro;

import java.time.LocalDate;
import java.util.List;

import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.dto.ActivityPatchRequestDto;
import com.gerson.projectpath_pro.activity.repository.dto.ActivityPostRequestDto;
import com.gerson.projectpath_pro.auth.controller.RegisterRequest;
import com.gerson.projectpath_pro.calculation.repository.Calculation;
import com.gerson.projectpath_pro.project.repository.Project;
import com.gerson.projectpath_pro.project.repository.dto.ProjectDto;
import com.gerson.projectpath_pro.user.repository.User;
import com.gerson.projectpath_pro.user.repository.dto.UserDto;

public class TestDataUtil {

    public static User createTestUserA() {
        return User.builder()
                .fullName("Pepe Le Pu")
                .username("onlypepe")
                .email("pepe@gmail.com")
                .dateOfBirth(LocalDate.of(1996, 4, 19))
                .password("peiapso")
                .build();
    }

    public static Project createTestProjectA(User user) {
        return Project.builder()
                .id(1L)
                .name("ProjectPath-Pro")
                .description("A project made to automatize the project network building and critical path calculation")
                .user(user)
                .build();
    }

    public static ProjectDto createTestProjectDtoA(UserDto userDto) {
        return ProjectDto.builder()
                .id(1L)
                .name("ProjectPath-Pro")
                .description("A project made to automatize the project network building and critical path calculation")
                .userDto(userDto)
                .build();
    }

    public static Project createTestProjectB(User user) {
        return Project.builder()
                .id(2L)
                .name("FitnessTracker")
                .description("A project made to keep track of your gym weights and PRs")
                .user(user)
                .build();
    }

    public static ProjectDto createTestProjectDtoB(UserDto userDto) {
        return ProjectDto.builder()
                .id(2L)
                .name("FitnessTracker")
                .description("A project made to keep track of your gym weights and PRs")
                .userDto(userDto)
                .build();
    }

    public static Project createTestProjectC(User user) {
        return Project.builder()
                .id(3L)
                .name("Healthcare AI")
                .description("A project made to predict a possible disease based on a list of 132 symptoms")
                .user(user)
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

    public static List<Activity> createActivityDatasetA(Project project) {
        return List.of(
                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("A")
                        .predecessors(null)
                        .daysDuration(3)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("B")
                        .predecessors("A")
                        .daysDuration(4)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("C")
                        .predecessors("A")
                        .daysDuration(6)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("D")
                        .predecessors("B")
                        .daysDuration(6)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("E")
                        .predecessors("B")
                        .daysDuration(4)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("F")
                        .predecessors("C")
                        .daysDuration(4)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("G")
                        .predecessors("D")
                        .daysDuration(6)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("H")
                        .predecessors("E,F")
                        .daysDuration(8)
                        .project(project)
                        .build());
    }

    public static List<Activity> createActivityDatasetB(Project project) {
        return List.of(
                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("A")
                        .predecessors(null)
                        .daysDuration(2)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("B")
                        .predecessors(null)
                        .daysDuration(5)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("C")
                        .predecessors(null)
                        .daysDuration(1)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("D")
                        .predecessors("B")
                        .daysDuration(10)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("E")
                        .predecessors("A,D")
                        .daysDuration(3)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("F")
                        .predecessors("C")
                        .daysDuration(6)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("PLACEHOLDER")
                        .label("G")
                        .predecessors("E,F")
                        .daysDuration(8)
                        .project(project)
                        .build());
    }

    public static List<Activity> createActivityDatasetC(Project project) {
        return List.of(
                Activity.builder()
                        .name("Aprobación de la solicitud")
                        .label("A")
                        .predecessors(null)
                        .daysDuration(5)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("Planes de construcción")
                        .label("B")
                        .predecessors("A")
                        .daysDuration(15)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("Estudio del Tránsito")
                        .label("C")
                        .predecessors("A")
                        .daysDuration(10)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("Verificación de la disponibilidad del servicio")
                        .label("D")
                        .predecessors("A")
                        .daysDuration(5)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("Reporte del personal")
                        .label("E")
                        .predecessors("B,C")
                        .daysDuration(15)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("Aprobación de la comisión")
                        .label("F")
                        .predecessors("B,C,D")
                        .daysDuration(10)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("Espera para construcción")
                        .label("G")
                        .predecessors("F")
                        .daysDuration(170)
                        .project(project)
                        .build(),

                Activity.builder()
                        .name("Ocupación")
                        .label("H")
                        .predecessors("E,G")
                        .daysDuration(35)
                        .project(project)
                        .build());
    }

    public static UserDto createTestUserDtoA() {
        return UserDto.builder()
                .id(1L)
                .fullName("Pepe Le Pu")
                .username("onlypepe")
                .email("pepe@gmail.com")
                .dateOfBirth(LocalDate.of(1996, 4, 19))
                .build();
    }

    public static UserDto createTestUserDtoB() {
        return UserDto.builder()
                .id(2L)
                .fullName("Juanito Alcachofa")
                .username("alcachofin")
                .email("alcachofin@gmail.com")
                .dateOfBirth(LocalDate.of(1996, 4, 19))
                .build();
    }

    public static UserDto createTestUserDtoC() {
        return UserDto.builder()
                .id(3L)
                .fullName("German Garmendia")
                .username("germansin")
                .email("germansin@gmail.com")
                .dateOfBirth(LocalDate.of(1996, 4, 19))
                .build();
    }

}
