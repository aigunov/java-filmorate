package ru.yandex.practicum.filmorate.storage.films_logic.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

public interface MPAStorage {
    MPA addMPA(MPA mpa);
    Optional<MPA> getMPA(int id);
    MPA deleteMPA(int id);
    MPA updateMPA(MPA mpa);
    List<MPA> getAllMPA();
    MPA getMPAofFilm(int filmdId);
}
