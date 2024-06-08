package ru.yandex.practicum.filmorate.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MPAService mpaService;

    @GetMapping //получить полный список рейтингов
    public Collection<MPA> getMpa() {
        return mpaService.getAllMpa();
    }

    @GetMapping("/{id}") //получить рейтинг по id
    public MPA getMpaById(@PathVariable int id) {
        return mpaService.getMpaById(id);
    }
}