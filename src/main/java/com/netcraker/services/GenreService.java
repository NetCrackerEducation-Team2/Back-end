package com.netcraker.services;

import com.netcraker.model.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getGenres();

    List<Genre> searchByNameContains(String genreNameContains);
}
