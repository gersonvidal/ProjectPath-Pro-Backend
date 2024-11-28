package com.gerson.projectpath_pro.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.dto.ActivityPostRequestDto;
import com.gerson.projectpath_pro.mappers.Mapper;

@Component
public class ActivityPostRequestMapperImpl implements Mapper<Activity, ActivityPostRequestDto> {

    private ModelMapper modelMapper;

    public ActivityPostRequestMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ActivityPostRequestDto mapTo(Activity activity) {
        return modelMapper.map(activity, ActivityPostRequestDto.class);
    }

    @Override
    public Activity mapFrom(ActivityPostRequestDto activityPostRequestDto) {
        Activity activity = modelMapper.map(activityPostRequestDto, Activity.class);
        activity.setId(null);

        return activity;
    }

}
