package com.gerson.projectpath_pro.activity.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityPatchRequestDto {

    private String name;

    // private String label;

    private String predecessors;

    private Integer daysDuration;

}
