package ru.yandex.practicum.filmorate.model;

import lombok.*;

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
    private String email;
    @NonNull
    private String login;
    private String name = "";
    @NonNull
    private LocalDate birthday;
}
