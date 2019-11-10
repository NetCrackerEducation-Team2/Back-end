package com.netcraker.repositories;

public interface BaseRepository<T, Id> {
    T getById(Id id);
    boolean insert(T entity);
    boolean update(T entity);
    boolean delete(Id id);
}