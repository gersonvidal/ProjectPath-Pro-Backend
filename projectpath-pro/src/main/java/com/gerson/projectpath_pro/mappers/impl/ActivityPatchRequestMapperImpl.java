package com.gerson.projectpath_pro.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.gerson.projectpath_pro.activity.repository.Activity;
import com.gerson.projectpath_pro.activity.repository.dto.ActivityPatchRequestDto;
import com.gerson.projectpath_pro.mappers.Mapper;

@Component
public class ActivityPatchRequestMapperImpl implements Mapper<Activity, ActivityPatchRequestDto> {

    private ModelMapper modelMapper;

    public ActivityPatchRequestMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ActivityPatchRequestDto mapTo(Activity activity) {
        return modelMapper.map(activity, ActivityPatchRequestDto.class);
    }

    @Override
    public Activity mapFrom(ActivityPatchRequestDto activityPatchRequestDto) {
        return modelMapper.map(activityPatchRequestDto, Activity.class);
    }

}
