package com.netcraker.controllers;

import com.netcraker.model.SearchingHistory;
import com.netcraker.model.wrapper.SearchingHistoryWrapper;
import com.netcraker.services.SearchingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/searching-history"})
@CrossOrigin(methods={RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class SearchingHistoryController {

    private final SearchingHistoryService searchingHistoryService;

    @PostMapping("/add")
    public ResponseEntity<?> addSearchingHistories(@RequestBody List<SearchingHistory> searchingHistories){
        searchingHistoryService.addSearchingHistories(searchingHistories);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
