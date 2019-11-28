package com.netcraker.services.events;

import com.netcraker.model.constants.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
public class DataBaseChangeEvent<ID extends Number> extends CustomEvent {
    private final TableName affectedTable;
    private final ID userId;
}

