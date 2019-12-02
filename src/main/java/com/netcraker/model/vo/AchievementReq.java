package com.netcraker.model.vo;

import com.netcraker.model.constants.Parameter;
import com.netcraker.model.constants.TableName;
import com.netcraker.model.constants.Verb;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AchievementReq {
    @Size(min = 4)
    private String name;
    @NotNull
    private Verb verb;
    @NotNull
    private TableName subject;
    @Nullable
    private Map<Parameter, List<String>> extraParams;
    @NotNull @Min(1)
    private Integer count;
    @Nullable
    private String description;
}
