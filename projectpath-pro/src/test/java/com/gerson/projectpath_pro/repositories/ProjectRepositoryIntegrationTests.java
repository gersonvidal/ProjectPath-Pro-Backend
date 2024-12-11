package com.gerson.projectpath_pro.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.gerson.projectpath_pro.TestDataUtil;
import com.gerson.projectpath_pro.project.repository.Project;
import com.gerson.projectpath_pro.project.repository.ProjectRepository;
import com.gerson.projectpath_pro.user.repository.User;
import com.gerson.projectpath_pro.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProjectRepositoryIntegrationTests {

    private ProjectRepository underTest;

    private UserRepository userRepository;

    @Autowired
    public ProjectRepositoryIntegrationTests(ProjectRepository underTest, UserRepository userRepository) {
        this.underTest = underTest;
        this.userRepository = userRepository;
    }

    @Transactional
    @Test
    public void testThatProjectCanBeCreatedAndRecalled() {
        User savedUser = userRepository.save(TestDataUtil.createTestUserA());
        Project project = TestDataUtil.createTestProjectA(savedUser);
        underTest.save(project);

        Optional<Project> result = underTest.findById(project.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(project);
    }

    @Transactional
    @Test
    public void testThatMultipleProjectsCanBeCreatedAndRecalled() {
        User savedUser = userRepository.save(TestDataUtil.createTestUserA());

        Project projectA = TestDataUtil.createTestProjectA(savedUser);
        underTest.save(projectA);

        Project projectB = TestDataUtil.createTestProjectB(savedUser);
        underTest.save(projectB);

        Project projectC = TestDataUtil.createTestProjectC(savedUser);
        underTest.save(projectC);

        Iterable<Project> result = underTest.findAll();

        assertThat(result)
                .hasSize(3)
                .containsExactly(projectA, projectB, projectC);
    }

    @Transactional
    @Test
    public void testThatProjectCanBeUpdated() {
        User savedUser = userRepository.save(TestDataUtil.createTestUserA());

        Project projectA = TestDataUtil.createTestProjectA(savedUser);
        underTest.save(projectA);

        projectA.setName("UPDATED");
        underTest.save(projectA);

        Optional<Project> result = underTest.findById(projectA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(projectA);
    }

    @Test
    public void testThatProjectCanBeDeleted() {
        User savedUser = userRepository.save(TestDataUtil.createTestUserA());

        Project projectA = TestDataUtil.createTestProjectA(savedUser);
        underTest.save(projectA);

        underTest.deleteById(projectA.getId());

        Optional<Project> result = underTest.findById(projectA.getId());

        assertThat(result).isEmpty();
    }

}
