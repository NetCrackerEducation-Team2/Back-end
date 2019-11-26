package com.netcraker.services.impl;

import com.netcraker.model.SearchingHistory;
import com.netcraker.repositories.SearchingHistoryRepository;
import com.netcraker.services.SearchingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchingHistoryServiceImp implements SearchingHistoryService {

    private final SearchingHistoryRepository searchingHistoryRepository;

    @Override
    public void addSearchingHistories(List<SearchingHistory> searchingHistories) {
        searchingHistoryRepository.insert(searchingHistories);
    }
}
