package com.netcraker.services;

import com.netcraker.model.Author;
import com.netcraker.model.Genre;

import java.util.List;

public interface AuthorService {
    List<Author> getAuthors();
    List<Author> searchByNameContains(String authorFullNameStartsWith);
    List<Author> searchByNameContains(String authorFullNameStartsWith, int offset, int size);
}
