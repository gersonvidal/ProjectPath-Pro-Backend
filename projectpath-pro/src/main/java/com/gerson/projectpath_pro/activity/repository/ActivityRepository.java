package com.gerson.projectpath_pro.activity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {

    List<Activity> findByProjectId(Long projectId);

    boolean existsByLabelAndProjectId(String label, Long projectId);

    @Query("SELECT a.label FROM Activity a WHERE a.project.id = :projectId")
    List<String> findAllLabelsByProjectId(@Param("projectId") Long projectId);

}
