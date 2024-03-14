package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.utils.Genre;
import ru.yandex.practicum.filmorate.utils.Rating;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Film.
 */
@AllArgsConstructor
@Data
@Builder
public class Film {
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    private int id;
    @Positive
    private int rate;
    @NotNull
    private Genre genre;
    @NotNull
    private Rating rating;

}
