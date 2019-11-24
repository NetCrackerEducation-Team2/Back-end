package com.netcraker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBook {
    private int userBookId;
    private Integer bookId;
    private Integer userId;
    Boolean favoriteMark;
    Boolean readMark;
    LocalDateTime creationTime;
}
