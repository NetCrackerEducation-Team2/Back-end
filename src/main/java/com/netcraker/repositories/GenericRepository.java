package com.netcraker.repositories;

import com.netcraker.model.Entity;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T extends Entity> {
    Optional<T> getById(int id);
    Optional<T> insert(T entity);
    Optional<T> update(T entity);
    boolean delete(T entity);
    int getCount();
}
