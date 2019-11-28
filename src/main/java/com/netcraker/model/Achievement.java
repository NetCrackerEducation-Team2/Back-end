package com.netcraker.model;

import com.netcraker.model.constants.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Achievement {
    private int achievementId;
    private String name;
    private String sqlQuery;
    private TableName tableName;
    private String requirement;
}
