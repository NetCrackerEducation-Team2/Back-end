package com.netcraker.services.impl;

import com.netcraker.model.Author;
import com.netcraker.model.Book;
import com.netcraker.model.Genre;
import com.netcraker.model.Page;
import com.netcraker.repositories.BookRepository;
import com.netcraker.repositories.BooksRecommendationsRepository;
import com.netcraker.repositories.BooksSelectionRepository;
import com.netcraker.repositories.StatsRepository;
import com.netcraker.services.BookService;
import com.netcraker.services.BooksRecommendationsService;
import com.netcraker.services.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BooksRecommendationsServiceImp implements BooksRecommendationsService {

    private final BookRepository bookRepository;
    private final StatsRepository statsRepository;
    private final BooksSelectionRepository booksSelectionRepository;
    private final BooksRecommendationsRepository booksRecommendationsRepository;
    private final PageService pageService;
    private final BookService bookService;

    @Override
    public void prepareBooksRecommendations(int userId, int count) {
        booksSelectionRepository.clear();
        booksSelectionRepository.insert(count * 5);
        final Map<Integer, Double> genresStats = statsRepository.getGenresStats(userId);
        final Map<Integer, Double> authorsStats = statsRepository.getAuthorsStats(userId);
        List<Book> selection = booksSelectionRepository.select();
        selection.forEach(bookRepository::loadReferences);
        Map<Book, Double> map = selection.stream().collect(Collectors.toMap(k -> k, v -> 1.0));
        map.replaceAll((book, stat) ->
                stat *= (1 + getAverageGenreStat(book, genresStats)) *
                        (1 + getAverageAuthorStat(book, authorsStats)) *
                        (1 + Math.exp((double) Duration.between(LocalDateTime.now(), book.getCreationTime()).toDays() / 30)));
        List<Book> recommendations = new ArrayList<>(map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(count)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                .keySet());
        booksRecommendationsRepository.clear();
        booksRecommendationsRepository.insert(recommendations);
    }

    @Override
    public Page<Book> getBooksRecommendations(int userId, int page, int pageSize) {
        int total = booksRecommendationsRepository.count();
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        List<Book> recommendations = booksRecommendationsRepository.select(pageSize, offset);
        recommendations.forEach(bookRepository::loadReferences);
        recommendations.forEach(bookService::insureBookPhoto);
        return new Page<>(currentPage, pagesCount, pageSize, recommendations);
    }

    private Double getAverageGenreStat(Book book, Map<Integer, Double> genresStats){
        double sum = 0;
        List<Genre> genres = book.getGenres();
        if(genres.isEmpty()){
            return 0.0;
        }
        for(Genre genre : genres){
            sum += genresStats.get(genre.getGenreId()) == null ? 0 : genresStats.get(genre.getGenreId());
        }
        return sum / genres.size();
    }

    private Double getAverageAuthorStat(Book book, Map<Integer, Double> authorStats){
        double sum = 0;
        List<Author> authors = book.getAuthors();
        if(authors.isEmpty()){
            return 0.0;
        }
        for(Author author : authors){
            sum += authorStats.get(author.getAuthorId()) == null ? 0 : authorStats.get(author.getAuthorId());
        }
        return sum / authors.size();
    }
}
