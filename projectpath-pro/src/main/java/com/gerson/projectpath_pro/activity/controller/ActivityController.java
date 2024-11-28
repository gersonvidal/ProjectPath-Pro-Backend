package com.gerson.projectpath_pro.activity.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.dto.ActivityDto;
import com.gerson.projectpath_pro.activity.repository.dto.ActivityPostRequestDto;
import com.gerson.projectpath_pro.activity.repository.dto.ActivityPatchRequestDto;
import com.gerson.projectpath_pro.activity.service.ActivityService;
import com.gerson.projectpath_pro.mappers.Mapper;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private ActivityService activityService;

    private Mapper<Activity, ActivityDto> activityMapper;

    private Mapper<Activity, ActivityPostRequestDto> activityPostRequestMapper;

    private Mapper<Activity, ActivityPatchRequestDto> activityPatchRequestMapper;

    private final String REGEX_PATTERN = "^([A-Z])(,([A-Z]))*$";

    public ActivityController(ActivityService activityService, Mapper<Activity, ActivityDto> activityMapper,
            Mapper<Activity, ActivityPostRequestDto> activityPostRequestMapper,
            Mapper<Activity, ActivityPatchRequestDto> activityPatchRequestMapper) {
        this.activityService = activityService;
        this.activityMapper = activityMapper;
        this.activityPostRequestMapper = activityPostRequestMapper;
        this.activityPatchRequestMapper = activityPatchRequestMapper;
    }

    // TODO: Validate label is in uppercase
    @PostMapping
    public ResponseEntity<ActivityDto> createActivity(@RequestBody ActivityPostRequestDto activityPostRequestDto) {

        if (activityPostRequestDto.getName() == null || activityPostRequestDto.getName().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (activityPostRequestDto.getLabel() == null || activityPostRequestDto.getLabel().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (activityPostRequestDto.getPredecessors() != null
                && !activityPostRequestDto.getPredecessors().matches(REGEX_PATTERN)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (activityPostRequestDto.getDaysDuration() == null || activityPostRequestDto.getDaysDuration() < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (activityPostRequestDto.getProjectDto().getId() == null
                || activityPostRequestDto.getProjectDto().getId() < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Activity activity = activityPostRequestMapper.mapFrom(activityPostRequestDto);

        Activity savedActivity = activityService.save(activity);

        return new ResponseEntity<>(activityMapper.mapTo(savedActivity),
                HttpStatus.CREATED);
    }

    @GetMapping
    public List<ActivityDto> listActivities() {
        List<Activity> activities = activityService.findAll();

        return activities.stream()
                .map(activityMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ActivityDto> getActivity(@PathVariable("id") Long id) {
        Optional<Activity> foundActivity = activityService.findById(id);

        return foundActivity.map(activity -> {
            ActivityDto activityDto = activityMapper.mapTo(activity);
            return new ResponseEntity<>(activityDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<List<ActivityDto>> getActivitiesByProjectId(@PathVariable("id") Long projectId) {

        if (projectId == null || projectId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Activity> activities = activityService.getActivitiesByProjectId(projectId);

        List<ActivityDto> activityDtos = activities.stream()
                .map(activityMapper::mapTo)
                .collect(Collectors.toList());

        return new ResponseEntity<>(activityDtos, HttpStatus.OK);

    }

    // @PutMapping(path = "/{id}")
    // public ResponseEntity<ActivityDto> fullUpdateActivity(
    // @PathVariable("id") Long id,
    // @RequestBody ActivityDto activityDto) {

    // if (!activityService.isExists(id)) {
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    // activityDto.setId(id);
    // Activity activity = activityMapper.mapFrom(activityDto);

    // Activity savedActivity = activityService.save(activity);

    // return new ResponseEntity<>(
    // activityMapper.mapTo(savedActivity),
    // HttpStatus.OK);
    // }

    // TODO: Update only predecessors
    @PatchMapping(path = "/{id}")
    public ResponseEntity<ActivityDto> partialUpdate(
            @PathVariable("id") Long id,
            @RequestBody ActivityPatchRequestDto activityPatchRequestDto) {

        if (!activityService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (activityPatchRequestDto.getName() != null && activityPatchRequestDto.getName().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (activityPatchRequestDto.getLabel() != null && activityPatchRequestDto.getLabel().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (activityPatchRequestDto.getPredecessors() != null
                && !activityPatchRequestDto.getPredecessors().matches(REGEX_PATTERN)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (activityPatchRequestDto.getDaysDuration() != null && activityPatchRequestDto.getDaysDuration() < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Activity activity = activityPatchRequestMapper.mapFrom(activityPatchRequestDto);
        System.out.println(activity);

        Activity updatedActivity = activityService.partialUpdate(id, activity);

        return new ResponseEntity<>(
                activityMapper.mapTo(updatedActivity),
                HttpStatus.OK);

    }

    // TODO: Delete in database all predecessors that had the eliminated activity
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteActivity(@PathVariable("id") Long id) {
        activityService.delete(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
