package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Film.
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
public class Film {
    @NotBlank(message = "The movie name cannot be empty")
    private String name;
    @NotBlank(message = "У фильма должно быть описание")
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Set<Integer> likesId;
    @Valid
    @NotNull
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    @Valid
    private MPA mpa;
    private Integer id;
    private int rate;

}
