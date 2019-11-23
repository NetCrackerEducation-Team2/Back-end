package com.netcraker.controllers;

import com.netcraker.model.Genre;
import com.netcraker.repositories.GenreRepository;
import com.netcraker.services.GenreService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api"})
@CrossOrigin(methods={RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public ResponseEntity<List<Genre>> getGenres(){
        List<Genre> genres = genreService.getGenres();
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    @GetMapping("/genres/searchByNameStartsWith")
    public List<Genre> searchByNameStartsWith(@RequestParam(name = "startsWith") String genreNameStartsWith) {
        return genreService.searchByNameStartsWith(genreNameStartsWith);
    }
}
