package com.netcraker.repositories;

import com.netcraker.model.Book;

import java.time.LocalDateTime;
import java.util.List;

public interface BookRepository extends BaseRepository<Book> {
    List<Book> getByName(String name, int size, int offset);
    List<Book> getByAuthor(int authorId, int size, int offset);
    List<Book> getByGenre(int genreId, int size, int offset);
    List<Book> getByAnnouncementDate(LocalDateTime date, int size, int offset);
    boolean addGenre(int bookId, int genreId);
    boolean addAuthor(int bookId, int authorId);
}
