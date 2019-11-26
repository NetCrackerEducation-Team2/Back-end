package com.netcraker.repositories.impl;

import com.netcraker.model.extractor.AuthorStatsExtractor;
import com.netcraker.model.extractor.GenreStatsExtractor;
import com.netcraker.repositories.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class StatsRepositoryImp implements StatsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${genres.getStats}")
    private String sqlGetGenresStats;
    @Value("${authors.getStats}")
    private String sqlGetAuthorsStats;

    @Override
    public Map<Integer, Double> getGenresStats(int userId) {
        return jdbcTemplate.query(sqlGetGenresStats, new GenreStatsExtractor(), userId, userId);
    }

    @Override
    public Map<Integer, Double> getAuthorsStats(int userId) {
        return jdbcTemplate.query(sqlGetAuthorsStats, new AuthorStatsExtractor(), userId, userId);
    }
}
