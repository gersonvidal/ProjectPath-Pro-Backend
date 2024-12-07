package com.gerson.projectpath_pro.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.dto.ActivityDto;
import com.gerson.projectpath_pro.mappers.Mapper;

@Component
public class ActivityMapperImpl implements Mapper<Activity, ActivityDto> {

    private ModelMapper modelMapper;

    public ActivityMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ActivityDto mapTo(Activity activity) {
        return modelMapper.map(activity, ActivityDto.class);
    }

    @Override
    public Activity mapFrom(ActivityDto activityDto) {
        return modelMapper.map(activityDto, Activity.class);
    }

}
