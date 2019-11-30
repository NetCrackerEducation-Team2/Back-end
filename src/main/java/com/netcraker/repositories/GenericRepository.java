package com.netcraker.repositories;

import org.springframework.jdbc.core.RowMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, RM> {
    Optional<T> getById(RM rowMapper, String sql, int id);
    Optional<T> insert(Object[] params, String sql, String idName);
}
