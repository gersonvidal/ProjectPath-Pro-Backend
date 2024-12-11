package com.gerson.projectpath_pro.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.gerson.projectpath_pro.TestAuthUtil;
import com.gerson.projectpath_pro.TestDataUtil;
import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.ActivityRepository;
import com.gerson.projectpath_pro.activity.service.ActivityService;
import com.gerson.projectpath_pro.calculation.repository.Calculation;
import com.gerson.projectpath_pro.calculation.repository.CalculationRepository;
import com.gerson.projectpath_pro.calculation.service.CalculationService;
import com.gerson.projectpath_pro.project.repository.Project;
import com.gerson.projectpath_pro.project.service.ProjectService;
import com.gerson.projectpath_pro.user.repository.User;
import com.gerson.projectpath_pro.user.service.UserService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class CalculationControllerIntegrationTests {

    private CalculationService calculationService;

    private ActivityService activityService;

    private ProjectService projectService;

    private UserService userService;

    private ActivityRepository activityRepository;

    private MockMvc mockMvc;

    @Autowired
    private TestAuthUtil testAuthUtil;

    @Autowired
    public CalculationControllerIntegrationTests(
            CalculationService calculationService,
            ActivityService activityService,
            ProjectService projectService,
            UserService userService,
            ActivityRepository activityRepository,
            CalculationRepository calculationRepository,
            MockMvc mockMvc) {
        this.calculationService = calculationService;
        this.activityService = activityService;
        this.projectService = projectService;
        this.userService = userService;
        this.activityRepository = activityRepository;
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatPostDatasetAReturnsCorrectEstimatedDurationAndCriticalPathCalculation() throws Exception {
        String testJwtToken = testAuthUtil.generateTestJwtToken();
        User user = userService.findByEmail("pepe@gmail.com").get();

        Project savedProject = projectService.save(TestDataUtil.createTestProjectA(user));
        Long projectId = savedProject.getId();

        List<Activity> activities = TestDataUtil.createActivityDatasetA(savedProject);
        activityRepository.saveAll(activities);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/calculations/project/" + projectId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken));

        Optional<Calculation> optionalCalculation = calculationService.getCalculationByProjectId(projectId);

        assertTrue(optionalCalculation.isPresent(), "Calculation was not saved in database");
        Calculation calculation = optionalCalculation.get();

        int expectedEstimatedDuration = 21;
        String expectedCriticalPath = "A-C-F-H";

        assertEquals(expectedEstimatedDuration, calculation.getEstimatedDuration(),
                "Estimated duration is not correct");
        assertEquals(expectedCriticalPath, calculation.getCriticalPath(), "Critical Path is not correct");
    }

    @Test
    public void testThatPostDatasetBReturnsCorrectEstimatedDurationAndCriticalPathCalculation() throws Exception {
        String testJwtToken = testAuthUtil.generateTestJwtToken();
        User user = userService.findByEmail("pepe@gmail.com").get();

        Project savedProject = projectService.save(TestDataUtil.createTestProjectB(user));
        Long projectId = savedProject.getId();

        List<Activity> activities = TestDataUtil.createActivityDatasetB(savedProject);
        activityRepository.saveAll(activities);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/calculations/project/" + projectId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken));

        Optional<Calculation> optionalCalculation = calculationService.getCalculationByProjectId(projectId);

        assertTrue(optionalCalculation.isPresent(), "Calculation was not saved in database");
        Calculation calculation = optionalCalculation.get();

        int expectedEstimatedDuration = 26;
        String expectedCriticalPath = "B-D-E-G";

        assertEquals(expectedEstimatedDuration, calculation.getEstimatedDuration(),
                "Estimated duration is not correct");
        assertEquals(expectedCriticalPath, calculation.getCriticalPath(), "Critical Path is not correct");
    }

    @Test
    public void testThatPostDatasetCReturnsCorrectEstimatedDurationAndCriticalPathCalculation() throws Exception {
        String testJwtToken = testAuthUtil.generateTestJwtToken();
        User user = userService.findByEmail("pepe@gmail.com").get();

        Project savedProject = projectService.save(TestDataUtil.createTestProjectC(user));
        Long projectId = savedProject.getId();

        List<Activity> activities = TestDataUtil.createActivityDatasetC(savedProject);
        activityRepository.saveAll(activities);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/calculations/project/" + projectId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken));

        Optional<Calculation> optionalCalculation = calculationService.getCalculationByProjectId(projectId);

        assertTrue(optionalCalculation.isPresent(), "Calculation was not saved in database");
        Calculation calculation = optionalCalculation.get();

        int expectedEstimatedDuration = 235;
        String expectedCriticalPath = "A-B-F-G-H";

        assertEquals(expectedEstimatedDuration, calculation.getEstimatedDuration(),
                "Estimated duration is not correct");
        assertEquals(expectedCriticalPath, calculation.getCriticalPath(), "Critical Path is not correct");
    }

    @Test
    public void testThatPostIncompleteDatasetReturns404NotFound() throws Exception {
        String testJwtToken = testAuthUtil.generateTestJwtToken();
        User user = userService.findByEmail("pepe@gmail.com").get();

        Project savedProject = projectService.save(TestDataUtil.createTestProjectC(user));
        Long projectId = savedProject.getId();

        List<Activity> activities = new ArrayList<>(TestDataUtil.createActivityDatasetC(savedProject));
        activities.remove(activities.get(4)); // remove Activity E
        activityRepository.saveAll(activities);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/calculations/project/" + projectId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void testThatPatchDatasetAReturnsCorrectEstimatedDurationAndCriticalPathCalculation() throws Exception {
        String testJwtToken = testAuthUtil.generateTestJwtToken();
        User user = userService.findByEmail("pepe@gmail.com").get();

        Project savedProject = projectService.save(TestDataUtil.createTestProjectA(user));
        Long projectId = savedProject.getId();

        List<Activity> activities = TestDataUtil.createActivityDatasetA(savedProject);
        activityRepository.saveAll(activities);

        // First create the activity calculations
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/calculations/project/" + projectId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken));

        // Then update activity A
        Activity activityA = activities.get(0);

        activityA.setDaysDuration(4); // From 3 to 4

        activityService.partialUpdate(activityA.getId(), activityA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/calculations/project/diagram/" + projectId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken));

        Optional<Calculation> optionalCalculation = calculationService.getCalculationByProjectId(projectId);

        assertTrue(optionalCalculation.isPresent(), "Calculation was not saved in database");
        Calculation calculation = optionalCalculation.get();

        int expectedEstimatedDuration = 22;
        String expectedCriticalPath = "A-C-F-H";

        assertEquals(expectedEstimatedDuration, calculation.getEstimatedDuration(),
                "Estimated duration is not correct");
        assertEquals(expectedCriticalPath, calculation.getCriticalPath(), "Critical Path is not correct");
    }

    @Test
    public void testThatPostDatasetAReturnsBase64String() throws Exception {
        String testJwtToken = testAuthUtil.generateTestJwtToken();
        User user = userService.findByEmail("pepe@gmail.com").get();

        Project savedProject = projectService.save(TestDataUtil.createTestProjectA(user));
        Long projectId = savedProject.getId();

        List<Activity> activities = TestDataUtil.createActivityDatasetA(savedProject);
        activityRepository.saveAll(activities);

        String base64Response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/calculations/project/" + projectId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify that the response is a Base64 encoded String
        assertNotNull(base64Response, "Controller's response was null");
        assertTrue(isBase64(base64Response), "Response is not a Base64 encoded stirng");
    }

    @Test
    public void testThatPatchDatasetAReturnsBase64String() throws Exception {
        String testJwtToken = testAuthUtil.generateTestJwtToken();
        User user = userService.findByEmail("pepe@gmail.com").get();

        Project savedProject = projectService.save(TestDataUtil.createTestProjectA(user));
        Long projectId = savedProject.getId();

        List<Activity> activities = TestDataUtil.createActivityDatasetA(savedProject);
        activityRepository.saveAll(activities);

        // First create the activity calculations
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/calculations/project/" + projectId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken));

        // Then update activity A
        Activity activityA = activities.get(0);

        activityA.setDaysDuration(4); // From 3 to 4

        activityService.partialUpdate(activityA.getId(), activityA);

        String base64Response = mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/calculations/project/diagram/" + projectId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                .andReturn()
                .getResponse()
                .getContentAsString();
        ;

        // Verify that the response is a Base64 encoded String
        assertNotNull(base64Response, "Controller's response was null");
        assertTrue(isBase64(base64Response), "Response is not a Base64 encoded stirng");
    }

    @Test
    public void testThatGetDatasetAReturnsBase64String() throws Exception {
        String testJwtToken = testAuthUtil.generateTestJwtToken();
        User user = userService.findByEmail("pepe@gmail.com").get();

        Project savedProject = projectService.save(TestDataUtil.createTestProjectA(user));
        Long projectId = savedProject.getId();

        List<Activity> activities = TestDataUtil.createActivityDatasetA(savedProject);
        activityRepository.saveAll(activities);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/calculations/project/" + projectId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken));

        String base64Response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/calculations/project/diagram" + projectId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                .andReturn()
                .getResponse()
                .getContentAsString();
        ;

        // Verify that the response is a Base64 encoded String
        assertNotNull(base64Response, "Controller's response was null");
        assertTrue(isBase64(base64Response), "Response is not a Base64 encoded stirng");
    }

    private boolean isBase64(String response) {
        try {
            Base64.getDecoder().decode(response);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
