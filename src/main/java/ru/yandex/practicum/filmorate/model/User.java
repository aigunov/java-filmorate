package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

/**
 * User
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class User {
    private final int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
