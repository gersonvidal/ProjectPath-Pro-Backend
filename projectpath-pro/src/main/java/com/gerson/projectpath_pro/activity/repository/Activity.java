package com.gerson.projectpath_pro.activity.repository;

import org.hibernate.annotations.Comment;

import com.gerson.projectpath_pro.project.repository.Project;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_id_seq")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Comment("note: The format starts with A-Z, then AA-ZZ, AAA-ZZZ, etc.")
    private String label;

    @Column(nullable = true)
    @Comment("note: The format is comma-separated, for example: \"A,B,C,D\"")
    private String predecessors;

    @Column(nullable = false)
    private Integer daysDuration;

    @Column(nullable = true)
    @Comment("note: The value corresponds to days")
    private Integer closeStart;

    @Column(nullable = true)
    @Comment("note: The value corresponds to days")
    private Integer distantStart;

    @Column(nullable = true)
    @Comment("note: The value corresponds to days")
    private Integer closeFinish;

    @Column(nullable = true)
    @Comment("note: The value corresponds to days")
    private Integer distantFinish;

    @Column(nullable = true)
    @Comment("note: The value corresponds to days")
    private Integer slack;

    @ManyToOne()
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

}
