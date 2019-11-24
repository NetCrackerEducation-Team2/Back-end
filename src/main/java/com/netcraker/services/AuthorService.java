package com.netcraker.services;

import com.netcraker.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAuthors();
    List<Author> searchByNameContains(String authorFullNameStartsWith);
}
