package com.gerson.projectpath_pro.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.gerson.projectpath_pro.TestAuthUtil;
import com.gerson.projectpath_pro.TestDataUtil;
import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.ActivityRepository;
import com.gerson.projectpath_pro.activity.service.ActivityService;
import com.gerson.projectpath_pro.calculation.service.CalculationService;
import com.gerson.projectpath_pro.project.repository.Project;
import com.gerson.projectpath_pro.project.service.ProjectService;
import com.gerson.projectpath_pro.user.repository.User;
import com.gerson.projectpath_pro.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CalculationServiceUnitTests {

    private CalculationService calculationService;

    private ProjectService projectService;

    private UserService userService;

    private ActivityService activityService;

    private ActivityRepository activityRepository;

    @Autowired
    private TestAuthUtil testAuthUtil;

    @Autowired
    public CalculationServiceUnitTests(
            CalculationService calculationService,
            ProjectService projectService,
            UserService userService,
            ActivityService activityService,
            ActivityRepository activityRepository) {
        this.calculationService = calculationService;
        this.projectService = projectService;
        this.userService = userService;
        this.activityService = activityService;
        this.activityRepository = activityRepository;
    }

    @Test
    public void testThatMakeAllCalculationsWhenNewReturnsCorrectDataForActivityHInDatasetA() {
        testAuthUtil.generateTestJwtToken();
        User user = userService.findByEmail("pepe@gmail.com").get();

        Project savedProject = projectService.save(TestDataUtil.createTestProjectA(user));
        Long projectId = savedProject.getId();

        List<Activity> activities = TestDataUtil.createActivityDatasetA(savedProject);
        activityRepository.saveAll(activities);

        calculationService.makeAllCalculationsWhenNew(projectId);

        List<Activity> foundActivities = activityService.getActivitiesByProjectId(projectId);

        Activity activityH = foundActivities.get(foundActivities.size() - 1);

        int expectedCloseStart = 13;
        int expectedCloseFinish = 21;
        int expectedDistantStart = 13;
        int expectedDistantFinish = 21;
        int expectedSlack = 0;

        assertEquals(expectedCloseStart, activityH.getCloseStart(),
                "Estimated duration is not correct");
        assertEquals(expectedCloseFinish, activityH.getCloseFinish(),
                "Estimated duration is not correct");
        assertEquals(expectedDistantStart, activityH.getDistantStart(),
                "Estimated duration is not correct");
        assertEquals(expectedDistantFinish, activityH.getDistantFinish(),
                "Estimated duration is not correct");
        assertEquals(expectedSlack, activityH.getSlack(),
                "Estimated duration is not correct");
    }

    @Transactional
    @Test
    public void testThatMakeAllCalculationsWhenAlreadyExistsReturnsCorrectDataForActivityAInDatasetA() {
        testAuthUtil.generateTestJwtToken();
        User user = userService.findByEmail("pepe@gmail.com").get();

        Project savedProject = projectService.save(TestDataUtil.createTestProjectA(user));
        Long projectId = savedProject.getId();

        List<Activity> activities = TestDataUtil.createActivityDatasetA(savedProject);

        // Update activityA
        Activity activityA = activities.get(0);
        activityA.setDaysDuration(4); // From 3 to 4

        activityRepository.saveAll(activities);

        calculationService.makeAllCalculationsWhenNew(projectId);

        List<Activity> foundActivities = activityService.getActivitiesByProjectId(projectId);

        System.out.println(foundActivities);

        // Return activity A to its original value for easier test
        activityA = foundActivities.get(0);

        activityA.setDaysDuration(3); // From 4 to 3

        activityService.save(activityA);

        calculationService.makeAllCalculationsWhenAlreadyExists(projectId);

        int expectedCloseStart = 0;
        int expectedCloseFinish = 3;
        int expectedDistantStart = 0;
        int expectedDistantFinish = 3;
        int expectedSlack = 0;

        assertEquals(expectedCloseStart, activityA.getCloseStart(),
                "Estimated duration is not correct");
        assertEquals(expectedCloseFinish, activityA.getCloseFinish(),
                "Estimated duration is not correct");
        assertEquals(expectedDistantStart, activityA.getDistantStart(),
                "Estimated duration is not correct");
        assertEquals(expectedDistantFinish, activityA.getDistantFinish(),
                "Estimated duration is not correct");
        assertEquals(expectedSlack, activityA.getSlack(),
                "Estimated duration is not correct");
    }

}
