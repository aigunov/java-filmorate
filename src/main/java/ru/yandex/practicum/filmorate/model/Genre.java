package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Genre {
    @Max(value = 6)
    @Min(value = 1)
    private int id;
    private String name;
}
