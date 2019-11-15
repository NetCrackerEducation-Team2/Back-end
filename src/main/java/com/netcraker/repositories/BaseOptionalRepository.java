package com.netcraker.repositories;

import java.util.Optional;

public interface BaseOptionalRepository<T> {
    Optional<T> getById(int id);
    Optional<T> insert(T entity);
    Optional<T> update(T entity);
    boolean delete(int id);
}
