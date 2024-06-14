package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * User.
 */
@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@Valid
@Builder(toBuilder = true)
public class User {
    @ToString.Exclude
    private final List<Integer> friends = new ArrayList<>();
    @ToString.Exclude
    private final List<Integer> likedFilms = new ArrayList<>();
    private int id;
    @NonNull
    @Email(message = "Invalid email format")
    @NotNull
    private String email;
    @NotNull
    private String login;
    private String name = "";
    @PastOrPresent(message = "This date has not been yet")
    @NonNull
    private LocalDate birthday;


}
