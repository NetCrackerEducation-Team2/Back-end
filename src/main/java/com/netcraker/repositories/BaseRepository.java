package com.netcraker.repositories;

public interface BaseRepository<T> {
    T getById(int id);
    T insert(T entity);
    T update(T entity);
    boolean delete(int id);
}