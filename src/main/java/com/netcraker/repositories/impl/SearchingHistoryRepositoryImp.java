package com.netcraker.repositories.impl;

import com.netcraker.model.SearchingHistory;
import com.netcraker.repositories.SearchingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class SearchingHistoryRepositoryImp implements SearchingHistoryRepository {

    private static final Logger logger = LoggerFactory.getLogger(SearchingHistoryRepositoryImp.class);
    private final JdbcTemplate jdbcTemplate;

    @Value("${searchingHistory.insert}")
    private String sqlInsert;

    @Override
    public void insert(List<SearchingHistory> searchingHistories) {
        this.jdbcTemplate.batchUpdate(sqlInsert, new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, searchingHistories.get(i).getBookId());
                ps.setInt(2, searchingHistories.get(i).getUserId());
            }

            public int getBatchSize() {
                return searchingHistories.size();
            }
        });
    }
}
