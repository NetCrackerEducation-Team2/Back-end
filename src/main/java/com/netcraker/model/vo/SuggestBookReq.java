package com.netcraker.model.vo;

import com.github.slugify.Slugify;
import com.netcraker.model.Author;
import com.netcraker.model.Book;
import com.netcraker.model.BookOverview;
import com.netcraker.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SuggestBookReq {
    private String title;
    private List<Author> authors;
    private List<Genre> genres;
    private String description;
    private int publishYear;
    private String release;
    private String isbn;
    private String publishingHouse;
    public Book convertToBook() {
        return Book.builder()
                .title(this.title)
                .authors(this.authors)
                .genres(this.genres)
                .release(LocalDate.parse(this.release))
                .isbn(Long.parseLong(this.isbn))
                .publishingHouse(this.publishingHouse)
                .slug(new Slugify().slugify(this.title))
                .build();
    }
    public BookOverview convertToBookOverview() {
        return BookOverview.builder()
                .description(this.description)
                .published(false)
                .creationTime(LocalDateTime.now())
                .build();
    }
}
