package com.netcraker.repositories;

import com.netcraker.model.Author;

import java.util.List;

public interface AuthorRepository extends BaseOptionalRepository<Author> {
    List<Author> getAll();
    List<Author> getByBook(int bookId);
    List<Author> searchByNameContains(String authorFullNameContains);
    List<Author> searchByNameContains(String authorFullNameContains, int offset, int size);
}
