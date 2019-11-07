package com.netcraker.repositories;

public interface BaseRepository<T, Id> {
    T getById(Id id);
    int insert(T entity);
    int update(T entity);
    int delete(Id id);
}