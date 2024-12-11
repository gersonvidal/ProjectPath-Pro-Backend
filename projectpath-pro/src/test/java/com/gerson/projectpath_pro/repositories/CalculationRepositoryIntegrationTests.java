package com.gerson.projectpath_pro.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.gerson.projectpath_pro.TestDataUtil;
import com.gerson.projectpath_pro.calculation.repository.Calculation;
import com.gerson.projectpath_pro.calculation.repository.CalculationRepository;
import com.gerson.projectpath_pro.project.repository.ProjectRepository;
import com.gerson.projectpath_pro.user.repository.User;
import com.gerson.projectpath_pro.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CalculationRepositoryIntegrationTests {

    private CalculationRepository underTest;

    private ProjectRepository projectRepository;

    private UserRepository userRepository;

    @Autowired
    public CalculationRepositoryIntegrationTests(CalculationRepository underTest, ProjectRepository projectRepository, UserRepository userRepository) {
        this.underTest = underTest;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Test
    public void testThatCalculationCanBeCreatedAndRecalled() {
        User savedUser = userRepository.save(TestDataUtil.createTestUserA());
        projectRepository.save(TestDataUtil.createTestProjectA(savedUser));

        Calculation calculation = TestDataUtil.createTestCalculationA(TestDataUtil.createTestProjectA(savedUser));
        underTest.save(calculation);

        Optional<Calculation> result = underTest.findById(calculation.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(calculation);
    }

    @Transactional
    @Test
    public void testThatMultipleCalculationsCanBeCreatedAndRecalled() {
        User savedUser = userRepository.save(TestDataUtil.createTestUserA());

        projectRepository.save(TestDataUtil.createTestProjectA(savedUser));
        projectRepository.save(TestDataUtil.createTestProjectB(savedUser));
        projectRepository.save(TestDataUtil.createTestProjectC(savedUser));

        Calculation calculationA = TestDataUtil.createTestCalculationA(TestDataUtil.createTestProjectA(savedUser));
        underTest.save(calculationA);

        Calculation calculationB = TestDataUtil.createTestCalculationB(TestDataUtil.createTestProjectB(savedUser));
        underTest.save(calculationB);

        Calculation calculationC = TestDataUtil.createTestCalculationC(TestDataUtil.createTestProjectC(savedUser));
        underTest.save(calculationC);

        Iterable<Calculation> result = underTest.findAll();

        assertThat(result)
                .hasSize(3)
                .containsExactly(calculationA, calculationB, calculationC);
    }

    @Transactional
    @Test
    public void testThatGetCalculationByProjectIdReturnsCalculation() {
        User savedUser = userRepository.save(TestDataUtil.createTestUserA());
        projectRepository.save(TestDataUtil.createTestProjectA(savedUser));

        Calculation calculation = TestDataUtil.createTestCalculationA(TestDataUtil.createTestProjectA(savedUser));
        underTest.save(calculation);

        Optional<Calculation> result = underTest.findByProjectId(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(calculation);
    }

    @Test
    public void testThatGetCalculationByProjectIdReturnsEmptyWhenNoCalculationExists() {
        Optional<Calculation> result = underTest.findByProjectId(1L);

        assertThat(result).isEmpty();
    }

    @Transactional
    @Test
    public void testThatCalculationCanBeUpdated() {
        User savedUser = userRepository.save(TestDataUtil.createTestUserA());
        projectRepository.save(TestDataUtil.createTestProjectA(savedUser));

        Calculation calculationA = TestDataUtil.createTestCalculationA(TestDataUtil.createTestProjectA(savedUser));
        underTest.save(calculationA);

        calculationA.setCriticalPath("L-M-N");
        underTest.save(calculationA);

        Optional<Calculation> result = underTest.findById(calculationA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(calculationA);
    }

    @Test
    public void testThatCalculationCanBeDeleted() {
        User savedUser = userRepository.save(TestDataUtil.createTestUserA());
        projectRepository.save(TestDataUtil.createTestProjectA(savedUser));

        Calculation calculationA = TestDataUtil.createTestCalculationA(TestDataUtil.createTestProjectA(savedUser));
        underTest.save(calculationA);

        underTest.deleteById(calculationA.getId());

        Optional<Calculation> result = underTest.findById(calculationA.getId());

        assertThat(result).isEmpty();
    }

}
