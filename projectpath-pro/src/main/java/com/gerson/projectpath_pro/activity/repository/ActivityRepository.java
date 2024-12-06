package com.gerson.projectpath_pro.activity.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {

    List<Activity> findByProjectId(Long projectId);

}
