package com.netcraker.repositories;

public interface BookGenreRepository {
    boolean insert(int bookId, int genreId);
    boolean delete(int bookId, int genreId);
}
