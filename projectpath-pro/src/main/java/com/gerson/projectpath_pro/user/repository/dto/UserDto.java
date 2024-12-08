package com.gerson.projectpath_pro.user.repository.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String fullName;

    private String username;

    private String email;

    private LocalDate dateOfBirth;

}
