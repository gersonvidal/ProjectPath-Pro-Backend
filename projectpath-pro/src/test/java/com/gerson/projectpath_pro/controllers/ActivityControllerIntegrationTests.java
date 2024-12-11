package com.gerson.projectpath_pro.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerson.projectpath_pro.TestAuthUtil;
import com.gerson.projectpath_pro.TestDataUtil;
import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.dto.ActivityPatchRequestDto;
import com.gerson.projectpath_pro.activity.repository.dto.ActivityPostRequestDto;
import com.gerson.projectpath_pro.activity.service.ActivityService;
import com.gerson.projectpath_pro.project.service.ProjectService;
import com.gerson.projectpath_pro.user.repository.User;
import com.gerson.projectpath_pro.user.service.UserService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ActivityControllerIntegrationTests {

        private ActivityService activityService;

        private ProjectService projectService;

        private UserService userService;

        private MockMvc mockMvc;

        private ObjectMapper objectMapper;

        @Autowired
        private TestAuthUtil testAuthUtil;

        @Autowired
        public ActivityControllerIntegrationTests(ActivityService activityService, ProjectService projectService,
                        UserService userService, MockMvc mockMvc) {
                this.activityService = activityService;
                this.projectService = projectService;
                this.userService = userService;
                this.mockMvc = mockMvc;
                this.objectMapper = new ObjectMapper();
        }

        @Test
        public void testThatCreateActivitySuccesfullyReturnsHttp201Created() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();
                User user = userService.findByEmail("pepe@gmail.com").get();

                projectService.save(TestDataUtil.createTestProjectA(user));

                ActivityPostRequestDto testActivityPostRequestDtoA = TestDataUtil
                                .createTestActivityPostRequestDtoA(TestDataUtil.createTestProjectDtoA(TestDataUtil.createTestUserDtoA()));

                String activityPostRequestDtoJson = objectMapper.writeValueAsString(testActivityPostRequestDtoA);

                mockMvc.perform(
                                MockMvcRequestBuilders.post("/api/activities")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(activityPostRequestDtoJson)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isCreated());
        }

        @Test
        public void testThatCreateActivitySuccesfullyReturnsSavedActivity() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();
                User user = userService.findByEmail("pepe@gmail.com").get();

                projectService.save(TestDataUtil.createTestProjectA(user));

                ActivityPostRequestDto testActivityPostRequestDtoA = TestDataUtil
                                .createTestActivityPostRequestDtoA(TestDataUtil.createTestProjectDtoA(TestDataUtil.createTestUserDtoA()));

                String activityPostRequestDtoJson = objectMapper.writeValueAsString(testActivityPostRequestDtoA);

                mockMvc.perform(
                                MockMvcRequestBuilders.post("/api/activities")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(activityPostRequestDtoJson)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.id").isNumber())
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.name")
                                                                .value("Security Investigation"))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.label").value(
                                                                "A"));
        }

        @Test
        public void testThatListActivitiesReturnsHttpStatus200() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.get("/api/activities")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isOk());

        }

        @Test
        public void testThatListActivitiesReturnsListOfActivities() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();
                User user = userService.findByEmail("pepe@gmail.com").get();

                projectService.save(TestDataUtil.createTestProjectA(user));

                Activity testActivityA = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA(user));
                activityService.save(testActivityA);

                mockMvc.perform(
                                MockMvcRequestBuilders.get("/api/activities")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$[0].name")
                                                                .value("Security Investigation"))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$[0].label").value(
                                                                "A"));
        }

        @Test
        public void testThatGetActivityReturnsHttpStatus200WhenActivityExists() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();
                User user = userService.findByEmail("pepe@gmail.com").get();

                projectService.save(TestDataUtil.createTestProjectA(user));

                Activity testActivityA = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA(user));
                activityService.save(testActivityA);

                mockMvc.perform(
                                MockMvcRequestBuilders.get("/api/activities/1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isOk());

        }

        @Test
        public void testThatGetActivityReturnsActivityWhenActivityExists() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();
                User user = userService.findByEmail("pepe@gmail.com").get();

                projectService.save(TestDataUtil.createTestProjectA(user));

                Activity testActivityA = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA(user));
                activityService.save(testActivityA);

                mockMvc.perform(
                                MockMvcRequestBuilders.get("/api/activities/1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.id").value(1))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.name")
                                                                .value("Security Investigation"))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.label").value(
                                                                "A"));

        }

        @Test
        public void testThatGetActivityReturnsHttpStatus404WhenActivityDoesntExists() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.get("/api/activities/1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isNotFound());

        }

        @Test
        public void testThatPartialUpdateExistingActivityReturnsHttpStatus200() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();
                User user = userService.findByEmail("pepe@gmail.com").get();

                projectService.save(TestDataUtil.createTestProjectA(user));

                Activity testActivityA = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA(user));
                Activity savedActivity = activityService.save(testActivityA);

                ActivityPatchRequestDto testActivityPatchRequestDtoA = TestDataUtil
                                .createTestActivityPatchRequestDtoA();

                String activityPatchRequestDtoJson = objectMapper.writeValueAsString(testActivityPatchRequestDtoA);

                mockMvc.perform(
                                MockMvcRequestBuilders.patch("/api/activities/" + savedActivity.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(activityPatchRequestDtoJson)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isOk());

        }

        @Test
        public void testThatPartialUpdateExistingActivityReturnsUpdatedActivity() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();
                User user = userService.findByEmail("pepe@gmail.com").get();

                projectService.save(TestDataUtil.createTestProjectA(user));

                Activity testActivityA = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA(user));
                Activity savedActivity = activityService.save(testActivityA);

                ActivityPatchRequestDto testActivityPatchRequestDtoA = TestDataUtil
                                .createTestActivityPatchRequestDtoA();

                String activityPatchRequestDtoJson = objectMapper.writeValueAsString(testActivityPatchRequestDtoA);

                mockMvc.perform(
                                MockMvcRequestBuilders.patch("/api/activities/" + savedActivity.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(activityPatchRequestDtoJson)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.id").value(savedActivity.getId()))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.name")
                                                                .value("UI"))
                                // Predecessors is null and shouldn't change
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.predecessors").value(
                                                                savedActivity.getPredecessors()))
                                // Days Duration is 3 and shouldn't change
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.daysDuration").value(
                                                                savedActivity.getDaysDuration()));

        }

        @Test
        public void testThatDeleteActivityReturnsHttpStatus404ForNonExistingActivity() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.delete("/api/activities/1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isNotFound());

        }

        @Test
        public void testThatDeleteActivityReturnsHttpStatus204ForExistingActivity() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();
                User user = userService.findByEmail("pepe@gmail.com").get();

                projectService.save(TestDataUtil.createTestProjectA(user));

                Activity testActivityA = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA(user));
                Activity savedActivity = activityService.save(testActivityA);

                mockMvc.perform(
                                MockMvcRequestBuilders.delete("/api/activities/" + savedActivity.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isNoContent());

        }

}
