package com.netcraker.services.events;

import com.netcraker.model.constants.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class DataBaseChangeEvent<ID extends Number> extends CustomEvent {
    private final TableName affectedTable;
    ID userId;
}

