package com.netcraker.repositories;

import java.util.Map;

public interface StatsRepository {
    Map<Integer, Double> getGenresStats(int userId);
    Map<Integer, Double> getAuthorsStats(int userId);
}
