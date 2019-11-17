package com.netcraker.services.impl;

import com.netcraker.model.Author;
import com.netcraker.repositories.AuthorRepository;
import com.netcraker.services.AuthorService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImp implements AuthorService {

    private final @NonNull AuthorRepository authorRepository;

    @Override
    public List<Author> getAuthors() {
        return authorRepository.getAll();
    }
}
