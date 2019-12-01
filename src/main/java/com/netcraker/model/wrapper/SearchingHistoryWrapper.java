package com.netcraker.model.wrapper;

import com.netcraker.model.SearchingHistory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SearchingHistoryWrapper {
    List<SearchingHistory> searchingHistories;
}
