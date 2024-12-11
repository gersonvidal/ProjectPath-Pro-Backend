package com.gerson.projectpath_pro.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.gerson.projectpath_pro.TestDataUtil;
import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.ActivityRepository;
import com.gerson.projectpath_pro.project.repository.ProjectRepository;
import com.gerson.projectpath_pro.user.repository.User;
import com.gerson.projectpath_pro.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ActivityRepositoryIntegrationTests {

    private ActivityRepository underTest;

    private ProjectRepository projectRepository;

    private UserRepository userRepository;

    @Autowired
    public ActivityRepositoryIntegrationTests(ActivityRepository underTest, ProjectRepository projectRepository,
            UserRepository userRepository) {
        this.underTest = underTest;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Test
    public void testThatActivityCanBeCreatedAndRecalled() {
        User savedUser = userRepository.save(TestDataUtil.createTestUserA());
        projectRepository.save(TestDataUtil.createTestProjectA(savedUser));

        Activity activity = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA(savedUser));
        underTest.save(activity);

        Optional<Activity> result = underTest.findById(activity.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(activity);
    }

    @Transactional
    @Test
    public void testThatMultipleActivitesCanBeCreatedAndRecalled() {
        User savedUser = userRepository.save(TestDataUtil.createTestUserA());

        projectRepository.save(TestDataUtil.createTestProjectA(savedUser));
        projectRepository.save(TestDataUtil.createTestProjectB(savedUser));
        projectRepository.save(TestDataUtil.createTestProjectC(savedUser));

        Activity activityA = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA(savedUser));
        underTest.save(activityA);

        Activity activityB = TestDataUtil.createTestActivityB(TestDataUtil.createTestProjectB(savedUser));
        underTest.save(activityB);

        Activity activityC = TestDataUtil.createTestActivityC(TestDataUtil.createTestProjectC(savedUser));
        underTest.save(activityC);

        Iterable<Activity> result = underTest.findAll();

        assertThat(result)
                .hasSize(3)
                .containsExactly(activityA, activityB, activityC);
    }

    @Transactional
    @Test
    public void testThatActivityCanBeUpdated() {
        User savedUser = userRepository.save(TestDataUtil.createTestUserA());
        projectRepository.save(TestDataUtil.createTestProjectA(savedUser));

        Activity activityA = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA(savedUser));
        underTest.save(activityA);

        activityA.setName("UPDATED");
        underTest.save(activityA);

        Optional<Activity> result = underTest.findById(activityA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(activityA);
    }

    @Test
    public void testThatActivityCanBeDeleted() {
        User savedUser = userRepository.save(TestDataUtil.createTestUserA());
        projectRepository.save(TestDataUtil.createTestProjectA(savedUser));

        Activity activityA = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA(savedUser));
        underTest.save(activityA);

        underTest.deleteById(activityA.getId());

        Optional<Activity> result = underTest.findById(activityA.getId());

        assertThat(result).isEmpty();
    }

}
