package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
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
    @ToString.Exclude
    private final List<Integer> friends = new ArrayList<>();
    @ToString.Exclude
    private final List<Integer> likedFilms = new ArrayList<>();
    private Integer id;
    @NonNull
    @Email(message = "Invalid email format")
    private String email;
    @NotNull
    @Pattern(regexp = "^[^\\s]+$", message = "The login must not contain spaces")
    private String login;
    private String name = "";
    @PastOrPresent(message = "This date has not been yet")
    @NonNull
    private LocalDate birthday;
}
