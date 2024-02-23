package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
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
    private List<User> friends = new ArrayList<>();
    private List<Film> likedFilms = new ArrayList<>();
}
