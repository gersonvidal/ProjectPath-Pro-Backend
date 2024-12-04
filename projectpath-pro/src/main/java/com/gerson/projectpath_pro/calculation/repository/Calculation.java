package com.gerson.projectpath_pro.calculation.repository;

import org.hibernate.annotations.Comment;

import com.gerson.projectpath_pro.project.repository.Project;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "calculations")
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calculation_id_seq")
    private Long id;
    
    @Column(nullable = false)
    @Comment("note: The format is hyphen-separated, for example: \"L-M-N-O-P\"")
    private String critical_path;
    
    @Column(nullable = false)
    @Comment("note: The value corresponds to days")
    private Integer estimated_duration;
    
    @OneToOne()
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

}
