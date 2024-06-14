package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.films.interfaces.MPAStorage;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MPAService {
    private final MPAStorage mpaStorage;

    public Collection<MPA> getAllMpa() {
        List<MPA> allMPA = mpaStorage.getAllMPA();
        log.info("Запрос на список всех MPA. Результат: {}", allMPA);
        return allMPA;
    }

    public MPA getMpaById(int id) {
        MPA mpa = mpaStorage.getMPA(id)
                .orElseThrow(() -> new NoSuchElementException("MPA с ID = " + id + " не найден"));
        return mpa;
    }
}
