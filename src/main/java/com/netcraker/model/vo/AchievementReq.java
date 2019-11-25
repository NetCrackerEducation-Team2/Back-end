package com.netcraker.model.vo;

import com.netcraker.model.constants.TableName;
import com.netcraker.model.constants.Verb;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AchievementReq {
    private String name;
    private Verb verb;
    private TableName subject;
    private int count;
}
