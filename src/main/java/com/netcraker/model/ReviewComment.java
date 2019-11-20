package com.netcraker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReviewComment {
    int commentId;
    @NotNull @Min(1) Integer authorId;
    @NotNull @Min(1) Integer bookReviewId;
    @NotBlank String content;
    private LocalDateTime creationTime;
}
