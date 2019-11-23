package com.netcraker.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityType {
    private int entityTypeId;
    private String name;
}
