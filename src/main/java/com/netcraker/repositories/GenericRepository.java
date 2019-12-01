package com.netcraker.repositories;

import java.util.Optional;

public interface GenericRepository<T, RM> {
    Optional<T> getById(Class<T> entity, RM rowMapper, int id);
    Optional<T> insert(T entity, RM rowMapper, Object[] params);
    Optional<T> update(T entity, RM rowMapper, Object[] params, int id);
    boolean delete(String entityName, int id);
}
