package com.netcraker.services.impl;

import com.netcraker.model.Genre;
import com.netcraker.repositories.GenreRepository;
import com.netcraker.services.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImp implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> getGenres() {
        return genreRepository.getAll();
    }

    @Override
    public List<Genre> searchByNameContains(String genreNameContains) {
        return genreRepository.findByNameContains(genreNameContains);
    }

    @Override
    public List<Genre> searchByNameContains(String genreNameContains, int offset, int size) {
        return genreRepository.findByNameContains(genreNameContains, offset, size);
    }
}
