
package com.gerson.projectpath_pro.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gerson.projectpath_pro.TestDataUtil;
import com.gerson.projectpath_pro.project.repository.Project;
import com.gerson.projectpath_pro.project.repository.ProjectRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProjectRepositoryIntegrationTests {

    private ProjectRepository underTest;

    @Autowired
    public ProjectRepositoryIntegrationTests(ProjectRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatProjectCanBeCreatedAndRecalled() {
        Project project = TestDataUtil.createTestProjectA();
        underTest.save(project);

        Optional<Project> result = underTest.findById(project.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(project);
    }

    @Test
    public void testThatMultipleProjectsCanBeCreatedAndRecalled() {
        Project projectA = TestDataUtil.createTestProjectA();
        underTest.save(projectA);

        Project projectB = TestDataUtil.createTestProjectB();
        underTest.save(projectB);

        Project projectC = TestDataUtil.createTestProjectC();
        underTest.save(projectC);

        Iterable<Project> result = underTest.findAll();

        assertThat(result)
                .hasSize(3)
                .containsExactly(projectA, projectB, projectC);
    }

    @Test
    public void testThatProjectCanBeUpdated() {
        Project projectA = TestDataUtil.createTestProjectA();
        underTest.save(projectA);

        projectA.setName("UPDATED");
        underTest.save(projectA);

        Optional<Project> result = underTest.findById(projectA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(projectA);
    }

    @Test
    public void testThatProjectCanBeDeleted() {
        Project projectA = TestDataUtil.createTestProjectA();
        underTest.save(projectA);

        underTest.deleteById(projectA.getId());

        Optional<Project> result = underTest.findById(projectA.getId());

        assertThat(result).isEmpty();
    }

}