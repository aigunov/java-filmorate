package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
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
    @NotEmpty
    @NotBlank(message = "The movie name cannot be empty")
    private final String name;
    @NotEmpty
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    @NotNull
    private final Set<Integer> likesId = new HashSet<>();
    private Integer id;
    @Positive
    @PositiveOrZero(message = "The duration of the film should be positive")
    private int rate;


}
