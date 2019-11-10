package com.netcraker.repositories;

public interface BaseRepository<T> {
    T getById(int id);
    boolean insert(T entity);
    boolean update(T entity);
    boolean delete(int id);
}