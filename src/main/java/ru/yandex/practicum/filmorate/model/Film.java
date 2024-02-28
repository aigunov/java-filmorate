package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @NotEmpty
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
    private int rate;


}
