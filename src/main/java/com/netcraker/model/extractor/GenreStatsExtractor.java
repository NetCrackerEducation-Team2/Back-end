package com.netcraker.model.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GenreStatsExtractor implements ResultSetExtractor<Map<Integer, Double>> {

    @Override
    public Map<Integer, Double> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Integer, Double> statsMap = new HashMap<>();
        double maxCount = 0;
        while (resultSet.next()){
            int genreId = resultSet.getInt("genre");
            double count = resultSet.getInt("count");
            statsMap.put(genreId, count);
            if(count > maxCount) {
                maxCount = count;
            }
        }
        final double normalizationValue = maxCount;
        statsMap.replaceAll((k, v) -> v /= normalizationValue);
        return statsMap;
    }
}
