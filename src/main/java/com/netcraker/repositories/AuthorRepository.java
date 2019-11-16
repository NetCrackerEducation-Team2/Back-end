package com.netcraker.repositories;

import com.netcraker.model.Author;

import java.util.List;

public interface AuthorRepository extends BaseOptionalRepository<Author> {
    List<Author> getAll();
    List<Author> getByBook(int bookId);
}
