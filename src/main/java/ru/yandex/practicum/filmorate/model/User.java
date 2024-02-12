package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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
    @NonNull
    @NotNull
    private String login;
    private String name = "";
    @NonNull
    private LocalDate birthday;
}
