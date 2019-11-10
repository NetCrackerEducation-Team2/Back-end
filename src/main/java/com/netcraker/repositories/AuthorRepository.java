package com.netcraker.repositories;

import com.netcraker.model.Author;

import java.util.List;

public interface AuthorRepository extends BaseRepository<Author, Integer> {
    List<Author> getByBook(int bookId);
}
