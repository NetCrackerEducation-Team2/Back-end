package com.netcraker.services.impl;

import com.ibm.icu.util.TimeUnit;
import com.netcraker.model.Author;
import com.netcraker.model.Book;
import com.netcraker.model.Genre;
import com.netcraker.model.Page;
import com.netcraker.repositories.BookRepository;
import com.netcraker.repositories.BooksRecommendationsRepository;
import com.netcraker.repositories.StatsRepository;
import com.netcraker.services.BookService;
import com.netcraker.services.BooksRecommendationsService;
import com.netcraker.services.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BooksRecommendationsServiceImp implements BooksRecommendationsService {

    private final BookRepository bookRepository;
    private final StatsRepository statsRepository;
    private final BooksRecommendationsRepository booksRecommendationsRepository;
    private final PageService pageService;

    @Override
    @Transactional
    public void prepareBooksRecommendations(int userId, int count) {
        int latestCount = booksRecommendationsRepository.count(userId);
        LocalDateTime latestUpdate = booksRecommendationsRepository.getLatestUpdateTime(userId);
        if(latestCount != count || latestCount == 0 || latestUpdate.isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
            final Map<Integer, Double> genresStats = statsRepository.getGenresStats(userId);
            final Map<Integer, Double> authorsStats = statsRepository.getAuthorsStats(userId);
            final List<Book> selection = bookRepository.getRandom(count * 5);
            selection.forEach(bookRepository::loadReferences);
            final Map<Book, Double> map = selection.stream().collect(Collectors.toMap(k -> k, v -> 1.0));
            map.replaceAll((book, stat) ->
                    stat /= (1 + getAverageGenreStat(book, genresStats)) *
                            (1 + getAverageAuthorStat(book, authorsStats)) *
                            (1 + Math.exp((double) Duration.between(LocalDateTime.now(), book.getCreationTime()).toDays() / 30)));
            List<Book> recommendations = map.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .limit(count)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                    .keySet()
                    .stream()
                    .sorted(Comparator.comparing(map::get))
                    .collect(Collectors.toList());
            booksRecommendationsRepository.clear(userId);
            booksRecommendationsRepository.insert(userId, recommendations);
        }
    }

    @Override
    public List<Page<Book>> getBooksRecommendations(int userId, int pageSize) {
        List<Book> recommendations = booksRecommendationsRepository.get(userId);
        recommendations.forEach(bookRepository::loadReferences);
        int total = recommendations.size();
        int pagesCount = pageService.getPagesCount(total, pageSize);
        List<Page<Book>> pages = new ArrayList<>();
        for (int p = 0; p < pagesCount; p++) {
            int currentPage = pageService.getRestrictedPage(p, pagesCount);
            int offset = currentPage * pageSize;
            Page<Book> page = new Page<>(currentPage, pagesCount, pageSize, recommendations.subList(offset, offset + pageSize));
            pages.add(page);
        }
        return pages;
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
