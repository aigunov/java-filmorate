package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikedFilm {
    private int userId;
    private int filmId;
}
