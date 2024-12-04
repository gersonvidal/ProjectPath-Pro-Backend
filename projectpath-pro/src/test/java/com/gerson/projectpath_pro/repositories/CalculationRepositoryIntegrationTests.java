package com.gerson.projectpath_pro.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gerson.projectpath_pro.TestDataUtil;
import com.gerson.projectpath_pro.calculation.repository.Calculation;
import com.gerson.projectpath_pro.calculation.repository.CalculationRepository;
import com.gerson.projectpath_pro.project.repository.ProjectRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CalculationRepositoryIntegrationTests {

    private CalculationRepository underTest;

    private ProjectRepository projectRepository;

    @Autowired
    public CalculationRepositoryIntegrationTests(CalculationRepository underTest, ProjectRepository projectRepository) {
        this.underTest = underTest;
        this.projectRepository = projectRepository;
    }

    @Test
    public void testThatCalculationCanBeCreatedAndRecalled() {
        projectRepository.save(TestDataUtil.createTestProjectA());

        Calculation calculation = TestDataUtil.createTestCalculationA(TestDataUtil.createTestProjectA());
        underTest.save(calculation);

        Optional<Calculation> result = underTest.findById(calculation.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(calculation);
    }

    @Test
    public void testThatMultipleCalculationsCanBeCreatedAndRecalled() {
        projectRepository.save(TestDataUtil.createTestProjectA());
        projectRepository.save(TestDataUtil.createTestProjectB());
        projectRepository.save(TestDataUtil.createTestProjectC());

        Calculation calculationA = TestDataUtil.createTestCalculationA(TestDataUtil.createTestProjectA());
        underTest.save(calculationA);

        Calculation calculationB = TestDataUtil.createTestCalculationB(TestDataUtil.createTestProjectB());
        underTest.save(calculationB);

        Calculation calculationC = TestDataUtil.createTestCalculationC(TestDataUtil.createTestProjectC());
        underTest.save(calculationC);

        Iterable<Calculation> result = underTest.findAll();

        assertThat(result)
                .hasSize(3)
                .containsExactly(calculationA, calculationB, calculationC);
    }

    @Test
    public void testThatCalculationCanBeUpdated() {
        projectRepository.save(TestDataUtil.createTestProjectA());

        Calculation calculationA = TestDataUtil.createTestCalculationA(TestDataUtil.createTestProjectA());
        underTest.save(calculationA);

        calculationA.setCritical_path("L-M-N");
        underTest.save(calculationA);

        Optional<Calculation> result = underTest.findById(calculationA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(calculationA);
    }

    @Test
    public void testThatCalculationCanBeDeleted() {
        projectRepository.save(TestDataUtil.createTestProjectA());

        Calculation calculationA = TestDataUtil.createTestCalculationA(TestDataUtil.createTestProjectA());
        underTest.save(calculationA);

        underTest.deleteById(calculationA.getId());

        Optional<Calculation> result = underTest.findById(calculationA.getId());

        assertThat(result).isEmpty();
    }

}
