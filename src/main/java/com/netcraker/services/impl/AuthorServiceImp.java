package com.netcraker.services.impl;

import com.netcraker.model.Author;
import com.netcraker.repositories.AuthorRepository;
import com.netcraker.services.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImp implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public List<Author> getAuthors() {
        return authorRepository.getAll();
    }

    @Override
    public List<Author> searchByNameContains(String authorFullNameStartsWith) {
        return authorRepository.searchByNameContains(authorFullNameStartsWith);
    }

    @Override
    public List<Author> searchByNameContains(String authorFullNameStartsWith, int offset, int size) {
        return authorRepository.searchByNameContains(authorFullNameStartsWith, offset, size);
    }
}
