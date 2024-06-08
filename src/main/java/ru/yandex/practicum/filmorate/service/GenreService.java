package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.films_logic.interfaces.GenreStorage;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;


    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> getAllGenres() {
        List<Genre> genres = genreStorage.getGenres();
        log.info("Запрашивается список жанров. Результат: {}", genres);
        return genres;
    }

    public Genre getGenre(int id) {
        Genre genre = genreStorage.getGenre(id)
                .orElseThrow(() -> new NoSuchElementException("Жанр с ID " + id + " не найден"));
        log.info("Запрашивается жанр с ID = {}. Рузельтат: {}", id, genre);
        return genre;
    }

}