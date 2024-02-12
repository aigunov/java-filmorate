package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@AllArgsConstructor
@Data
@Builder
public class Film {
    private int id;
    private int rate;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;

}
