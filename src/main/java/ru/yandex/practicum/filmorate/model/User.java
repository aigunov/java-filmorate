package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * User.
 */
@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@Builder
public class User {
    private int id;
    @NonNull
    @Email
    private String email;
    @NotNull
    private String login;
    private String name = "";
    @NonNull
    private LocalDate birthday;
    @ToString.Exclude private final List<Integer> friends = new ArrayList<>();
    @ToString.Exclude private final List<Film> likedFilms = new ArrayList<>();
}
