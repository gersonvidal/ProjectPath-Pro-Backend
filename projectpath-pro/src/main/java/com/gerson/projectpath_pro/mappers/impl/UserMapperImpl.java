package com.gerson.projectpath_pro.mappers.impl;

import org.springframework.stereotype.Component;

import com.gerson.projectpath_pro.mappers.Mapper;
import com.gerson.projectpath_pro.user.repository.User;
import com.gerson.projectpath_pro.user.repository.dto.UserDto;

@Component
public class UserMapperImpl implements Mapper<User, UserDto> {

    @Override
    public UserDto mapTo(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }

    @Override
    public User mapFrom(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        return User.builder()
                .id(userDto.getId())
                .fullName(userDto.getFullName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .dateOfBirth(userDto.getDateOfBirth())
                .build();
    }
}
