package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MPAService mpaService;

    @GetMapping //получить полный список рейтингов
    public Collection<MPA> getMpa() {
        log.info("пришел запрос GET /mpa на получение списка всех рейтингов");
        Collection<MPA> mpas = mpaService.getAllMpa();
        log.info("отправлен ответ на GET /mpa запрос с телом: {}", mpas);
        return mpas;
    }

    @GetMapping("/{id}") //получить рейтинг по id
    public MPA getMpaById(@PathVariable int id) {
        log.info("пришел запрос GET /mpa/{id} на получение MPA", id);
        MPA mpa = mpaService.getMpaById(id);
        log.info("отправлен ответ на GET /mpa/{id} с телом: {}", mpa);
        return mpa;
    }
}