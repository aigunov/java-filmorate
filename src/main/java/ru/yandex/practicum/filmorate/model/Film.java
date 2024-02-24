package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

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
    private int id;
    @Positive
    private int rate;
    @NotNull
    private int likeCount = 0;

}
