package com.netcraker.repositories;

import com.netcraker.model.Genre;

import java.util.List;

public interface GenreRepository extends BaseOptionalRepository<Genre> {
    List<Genre> getAll();
    List<Genre> getByBook(int bookId);
    List<Genre> findByNameContains(String genreNameContains);
}