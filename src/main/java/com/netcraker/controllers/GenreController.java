package com.netcraker.controllers;

import com.netcraker.model.Genre;
import com.netcraker.services.GenreService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/genres/searchByNameContains")
    public List<Genre> searchByNameStartsWith(@RequestParam(name = "contains") String genreNameContains) {
        return genreService.searchByNameContains(genreNameContains);
    }

    @GetMapping("/genres/searchPartByNameContains")
    public List<Genre> searchPartByNameStartsWith(@RequestParam(name = "contains") String genreNameContains,
                                                  @RequestParam int offset,
                                                  @RequestParam int size) {
        return genreService.searchByNameContains(genreNameContains, offset, size);
    }
}
