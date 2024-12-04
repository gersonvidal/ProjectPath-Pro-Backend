package com.gerson.projectpath_pro.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gerson.projectpath_pro.TestDataUtil;
import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.ActivityRepository;
import com.gerson.projectpath_pro.project.repository.ProjectRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ActivityRepositoryIntegrationTests {

    private ActivityRepository underTest;

    private ProjectRepository projectRepository;

    @Autowired
    public ActivityRepositoryIntegrationTests(ActivityRepository underTest, ProjectRepository projectRepository) {
        this.underTest = underTest;
        this.projectRepository = projectRepository;
    }

    @Test
    public void testThatActivityCanBeCreatedAndRecalled() {
        projectRepository.save(TestDataUtil.createTestProjectA());

        Activity activity = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA());
        underTest.save(activity);

        Optional<Activity> result = underTest.findById(activity.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(activity);
    }

    @Test
    public void testThatMultipleActivitesCanBeCreatedAndRecalled() {
        projectRepository.save(TestDataUtil.createTestProjectA());
        projectRepository.save(TestDataUtil.createTestProjectB());
        projectRepository.save(TestDataUtil.createTestProjectC());

        Activity activityA = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA());
        underTest.save(activityA);

        Activity activityB = TestDataUtil.createTestActivityB(TestDataUtil.createTestProjectB());
        underTest.save(activityB);

        Activity activityC = TestDataUtil.createTestActivityC(TestDataUtil.createTestProjectC());
        underTest.save(activityC);

        Iterable<Activity> result = underTest.findAll();

        assertThat(result)
                .hasSize(3)
                .containsExactly(activityA, activityB, activityC);
    }

    @Test
    public void testThatActivityCanBeUpdated() {
        projectRepository.save(TestDataUtil.createTestProjectA());

        Activity activityA = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA());
        underTest.save(activityA);

        activityA.setName("UPDATED");
        underTest.save(activityA);

        Optional<Activity> result = underTest.findById(activityA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(activityA);
    }

    @Test
    public void testThatActivityCanBeDeleted() {
        projectRepository.save(TestDataUtil.createTestProjectA());

        Activity activityA = TestDataUtil.createTestActivityA(TestDataUtil.createTestProjectA());
        underTest.save(activityA);

        underTest.deleteById(activityA.getId());

        Optional<Activity> result = underTest.findById(activityA.getId());

        assertThat(result).isEmpty();
    }

}
