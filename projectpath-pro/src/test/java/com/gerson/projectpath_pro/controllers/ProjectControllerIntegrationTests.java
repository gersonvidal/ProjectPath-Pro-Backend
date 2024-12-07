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
import com.gerson.projectpath_pro.project.repository.Project;
import com.gerson.projectpath_pro.project.repository.dto.ProjectDto;
import com.gerson.projectpath_pro.project.service.ProjectService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ProjectControllerIntegrationTests {

        private ProjectService projectService;

        private MockMvc mockMvc;

        private ObjectMapper objectMapper;

        @Autowired
        private TestAuthUtil testAuthUtil;

        @Autowired
        public ProjectControllerIntegrationTests(ProjectService projectService, MockMvc mockMvc) {
                this.projectService = projectService;
                this.mockMvc = mockMvc;
                this.objectMapper = new ObjectMapper();
        }

        @Test
        public void testThatCreateProjectSuccesfullyReturnsHttp201Created() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();

                Project testProjectA = TestDataUtil.createTestProjectA();
                testProjectA.setId(null);

                String projectJson = objectMapper.writeValueAsString(testProjectA);

                mockMvc.perform(
                                MockMvcRequestBuilders.post("/api/projects")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(projectJson)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isCreated());
        }

        @Test
        public void testThatCreateProjectSuccesfullyReturnsSavedProject() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();

                ProjectDto testProjectDtoA = TestDataUtil.createTestProjectDtoA();
                testProjectDtoA.setId(null);

                String projectDtoJson = objectMapper.writeValueAsString(testProjectDtoA);

                mockMvc.perform(
                                MockMvcRequestBuilders.post("/api/projects")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(projectDtoJson)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.id").isNumber())
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.name").value("ProjectPath-Pro"))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.description").value(
                                                                "A project made to automatize the project network building and critical path calculation"));
        }

        @Test
        public void testThatListProjectsReturnsHttpStatus200() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.get("/api/projects")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isOk());

        }

        @Test
        public void testThatListProjectsReturnsListOfProjects() throws Exception {
                Project testProjectA = TestDataUtil.createTestProjectA();
                projectService.save(testProjectA);

                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.get("/api/projects")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$[0].name").value("ProjectPath-Pro"))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$[0].description").value(
                                                                "A project made to automatize the project network building and critical path calculation"));
                ;

        }

        @Test
        public void testThatGetProjectReturnsHttpStatus200WhenProjectExists() throws Exception {
                Project testProjectA = TestDataUtil.createTestProjectA();
                projectService.save(testProjectA);

                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.get("/api/projects/1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isOk());

        }

        @Test
        public void testThatGetProjectReturnsProjectWhenProjectExists() throws Exception {
                Project testProjectA = TestDataUtil.createTestProjectA();
                projectService.save(testProjectA);

                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.get("/api/projects/1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.id").value(1))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.name").value("ProjectPath-Pro"))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.description").value(
                                                                "A project made to automatize the project network building and critical path calculation"));

        }

        @Test
        public void testThatGetProjectReturnsHttpStatus404WhenProjectDoesntExists() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.get("/api/projects/1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isNotFound());

        }

        @Test
        public void testThatFullUpdateProjectReturnsHttpStatus200WhenProjectExists() throws Exception {
                Project testProjectA = TestDataUtil.createTestProjectA();
                Project savedProject = projectService.save(testProjectA);

                ProjectDto testProjectDtoA = TestDataUtil.createTestProjectDtoA();
                String projectDtoJson = objectMapper.writeValueAsString(testProjectDtoA);

                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.put("/api/projects/" + savedProject.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(projectDtoJson)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isOk());

        }

        @Test
        public void testThatFullUpdateUpdatesExistingProject() throws Exception {
                Project testProjectA = TestDataUtil.createTestProjectA();
                Project savedProject = projectService.save(testProjectA);

                ProjectDto testProjectDtoB = TestDataUtil.createTestProjectDtoB();
                testProjectDtoB.setId(savedProject.getId());

                String projectDtoBJson = objectMapper.writeValueAsString(testProjectDtoB);

                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.put("/api/projects/" + savedProject.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(projectDtoBJson)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.id").value(savedProject.getId()))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.name")
                                                                .value(testProjectDtoB.getName()))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.description").value(
                                                                testProjectDtoB.getDescription()));
        }

        @Test
        public void testThatFullUpdateProjectReturnsHttpStatus404WhenProjectDoesntExists() throws Exception {
                ProjectDto testProjectDtoA = TestDataUtil.createTestProjectDtoA();
                String projectDtoJson = objectMapper.writeValueAsString(testProjectDtoA);

                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.put("/api/projects/1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(projectDtoJson)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isNotFound());

        }

        @Test
        public void testThatPartialUpdateExistingProjectReturnsHttpStatus200() throws Exception {
                Project testProjectA = TestDataUtil.createTestProjectA();
                Project savedProject = projectService.save(testProjectA);

                ProjectDto testProjectDtoA = TestDataUtil.createTestProjectDtoA();
                testProjectDtoA.setName("Facebook");

                String projectDtoJson = objectMapper.writeValueAsString(testProjectDtoA);

                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.patch("/api/projects/" + savedProject.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(projectDtoJson)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isOk());

        }

        @Test
        public void testThatPartialUpdateExistingProjectReturnsUpdatedProject() throws Exception {
                Project testProjectA = TestDataUtil.createTestProjectA();
                Project savedProject = projectService.save(testProjectA);

                ProjectDto testProjectDtoA = TestDataUtil.createTestProjectDtoA();
                testProjectDtoA.setName("Facebook");

                String projectDtoJson = objectMapper.writeValueAsString(testProjectDtoA);

                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.patch("/api/projects/" + savedProject.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(projectDtoJson)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.id").value(savedProject.getId()))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.name")
                                                                .value("Facebook"))
                                .andExpect(
                                                MockMvcResultMatchers.jsonPath("$.description").value(
                                                                testProjectDtoA.getDescription()));

        }

        @Test
        public void testThatDeleteProjectReturnsHttpStatus204ForNonExistingProject() throws Exception {
                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.delete("/api/projects/1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isNoContent());

        }

        @Test
        public void testThatDeleteProjectReturnsHttpStatus204ForExistingProject() throws Exception {
                Project testProjectA = TestDataUtil.createTestProjectA();
                Project savedProject = projectService.save(testProjectA);

                String testJwtToken = testAuthUtil.generateTestJwtToken();

                mockMvc.perform(
                                MockMvcRequestBuilders.delete("/api/projects/" + savedProject.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testJwtToken))
                                .andExpect(
                                                MockMvcResultMatchers.status().isNoContent());

        }

}
