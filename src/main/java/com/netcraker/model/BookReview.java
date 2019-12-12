package com.netcraker.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookReview {
    private int bookReviewId;
    @NotNull @Min(1) @Max(10)
    private Integer rating;
    @NotBlank
    private String description;
    @NotNull @Min(1)
    private Integer userId;
    @NotNull @Min(1)
    private Integer bookId;
    private boolean published;
    private Book book;
    private User user;
    private LocalDateTime creationTime;
}
