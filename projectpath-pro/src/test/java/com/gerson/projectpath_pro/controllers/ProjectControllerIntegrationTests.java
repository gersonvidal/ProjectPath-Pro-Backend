package com.gerson.projectpath_pro.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.gerson.projectpath_pro.user.User;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ProjectControllerIntegrationTests {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private TestAuthUtil testAuthUtil;

    @Autowired
    public ProjectControllerIntegrationTests(MockMvc mockMvc) {
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
                        MockMvcResultMatchers.jsonPath("$.description").value("A project made to automatize the project network building and critical path calculation"));
    }

}
