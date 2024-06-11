package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping//получить полный список жанров
    public Collection<Genre> getGenres() {
        log.info("пришел запрос GET /genres на получение списка всех фильмов");
        List<Genre> genres = genreService.getAllGenres();
        log.info("отправлен ответ на GET /genres запрос с телом: {}", genres);
        return genres;
    }

    @GetMapping("/{id}") //получить жанр по id
    public Genre getGenreById(@PathVariable int id) {
        log.info("пришел запрос GET /genres/{id} на получение жанра", id);
        Genre genre = genreService.getGenre(id);
        log.info("отправлен ответ на GET /genres/{id} запрос с телом: {}", id, genre);
        return genre;
    }
}