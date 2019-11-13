package com.netcraker.model.mapper;

import com.netcraker.model.Book;
import com.netcraker.repositories.AuthorRepository;
import com.netcraker.repositories.GenreRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@AllArgsConstructor
public class BookRowMapper implements RowMapper<Book> {

    private final @NonNull GenreRepository genreRepository;
    private final @NonNull AuthorRepository authorRepository;

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        Book book = Book.builder()
                .bookId(resultSet.getInt("book_id"))
                .title(resultSet.getString("title"))
                .isbn(resultSet.getInt("isbn"))
                .release(resultSet.getDate("release").toLocalDate())
                .pages(resultSet.getInt("pages"))
                .filePath(resultSet.getString("file_path"))
                .photoPath(resultSet.getString("photo_path"))
                .publishingHouse(resultSet.getString("publishing_house"))
                .rateSum(resultSet.getInt("rate_sum"))
                .votersCount(resultSet.getInt(("voters_count")))
                .creationTime(resultSet.getTimestamp("creation_time").toLocalDateTime())
                .slug(resultSet.getString("slug"))
                .authors(new ArrayList<>(authorRepository.getByBook(resultSet.getInt("book_id"))))
                .genres(new ArrayList<>(genreRepository.getByBook(resultSet.getInt("book_id"))))
                .build();
        return book;
    }
}
