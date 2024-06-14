package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.films.FilmDB;
import ru.yandex.practicum.filmorate.storage.films.interfaces.FilmStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmDBTest {
    private static final Integer TEST_FILM_ID = 1;
    private static final Integer TEST_FILM_ID_INSEPTION = 2;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Film filmPulpFiction, filmInception;

    @BeforeEach
    public void init() {
        filmPulpFiction = Film.builder()
                .id(TEST_FILM_ID)
                .name("Pulp Fiction")
                .description("Two killer gangsters and a mysterious suitcase")
                .duration(154)
                .releaseDate(LocalDate.of(1994, 4, 23))
                .mpa(new MPA(5, "NC-17"))
                .genres(new LinkedHashSet<>())
                .rate(10)
                .likesId(new LinkedHashSet<>())
                .build();
        filmInception = Film.builder()
                .id(TEST_FILM_ID_INSEPTION)
                .name("Inception")
                .description("Sleep")
                .duration(148)
                .mpa(new MPA(5, "NC-17"))
                .releaseDate(LocalDate.of(2010, 4, 23))
                .genres(new LinkedHashSet<>())
                .rate(10)
                .likesId(new LinkedHashSet<>())
                .build();
    }

    @Test
    public void check_addFilm_shouldAddFilm() {
        FilmStorage filmStorage = new FilmDB(jdbcTemplate);
        Film newFilm = filmStorage.addFilm(filmPulpFiction);
        assertEquals(filmPulpFiction, newFilm);
    }

    @Test
    public void check_updateFilm_shouldUpdateFilm() {
        FilmStorage filmStorage = new FilmDB(jdbcTemplate);
        Film newFilm = filmStorage.addFilm(filmPulpFiction);
        newFilm.setName("PULP FICTION!");
        filmStorage.updateFilm(newFilm);
        assertEquals(newFilm, filmStorage.getFilmById(TEST_FILM_ID).get());
        assertEquals("PULP FICTION!", filmStorage.getFilmById(TEST_FILM_ID).get().getName());
    }

    @Test
    public void check_deleteFilm_shouldDeleteFilm() {
        FilmStorage filmStorage = new FilmDB(jdbcTemplate);
        filmStorage.addFilm(filmPulpFiction);
        filmStorage.deleteFilmById(TEST_FILM_ID);
        assertThrows(NoSuchElementException.class, () -> filmStorage.getFilmById(TEST_FILM_ID).get());
    }

    @Test
    public void check_getAllFilms_shouldReturnAllFilms() {
        FilmStorage filmStorage = new FilmDB(jdbcTemplate);
        Film pulpFiction = filmStorage.addFilm(filmPulpFiction);
        Film inception = filmStorage.addFilm(filmInception);
        List<Film> films = filmStorage.getFilms();
        assertEquals(List.of(pulpFiction, inception), films);
    }
}
