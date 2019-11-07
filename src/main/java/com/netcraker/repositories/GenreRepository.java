package com.netcraker.repositories;

import com.netcraker.model.Genre;

import java.util.List;

public interface GenreRepository extends BaseRepository<Genre> {
    List<Genre> getByBook(int bookId);
}
