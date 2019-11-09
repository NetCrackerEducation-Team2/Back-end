package com.netcraker.repositories;

import com.netcraker.model.Book;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends BaseRepository<Book, Integer> {

    int countAll();
    List<Book> getAll(int size, int offset);
    int countByName(String name);
    List<Book> getByName(String name, int size, int offset);
    int countByAuthor(int authorId);
    List<Book> getByAuthor(int authorId, int size, int offset);
    int countByGenre(int genreId);
    List<Book> getByGenre(int genreId, int size, int offset);
    int countByAnnouncementDate(LocalDate date);
    List<Book> getByAnnouncementDate(LocalDate date, int size, int offset);
}
