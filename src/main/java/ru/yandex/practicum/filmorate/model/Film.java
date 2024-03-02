package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Builder
public class Film {
    @NotBlank(message = "The movie name cannot be empty")
    private final String name;
    @NotBlank(message = "У фильма должно быть описание")
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    private final Set<Integer> likesId = new HashSet<>();
    private Integer id;
    private int rate;


}
