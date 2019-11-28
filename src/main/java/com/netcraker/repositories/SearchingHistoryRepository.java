package com.netcraker.repositories;

import com.netcraker.model.SearchingHistory;

import java.util.List;

public interface SearchingHistoryRepository {
    void insert(List<SearchingHistory> searchingHistories);
}
