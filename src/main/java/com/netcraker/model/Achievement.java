package com.netcraker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Achievement {
    private int achievementId;
    @NotBlank
    private String name;
    @NotBlank
    private String requirement;
}
