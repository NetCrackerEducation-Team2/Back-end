package com.netcraker.repositories;

public interface BookAuthorRepository {
    boolean insert(int bookId, int authorId);
    boolean delete(int bookId, int authorId);
}
