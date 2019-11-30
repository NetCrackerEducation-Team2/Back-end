package com.netcraker.repositories;

import com.netcraker.model.Achievement;
import com.netcraker.model.constants.TableName;

import java.util.List;
import java.util.Optional;

public interface AchievementRepository {
    Optional<Achievement> getById(int id);
    Optional<Achievement> insert(Achievement entity);
    boolean delete(int id);

    Optional<Achievement> getByName(String name);
    List<Achievement> getByTableName(TableName tableName);
}
