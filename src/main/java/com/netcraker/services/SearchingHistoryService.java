package com.netcraker.services;

import com.netcraker.model.SearchingHistory;

import java.util.List;

public interface SearchingHistoryService {
    void addSearchingHistories(List<SearchingHistory> searchingHistories);
}
