package ru.poezdizm.timelinetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.poezdizm.timelinetracker.entity.CharacterEntity;
import ru.poezdizm.timelinetracker.repository.CharacterRepository;

import java.util.List;

@Controller
@RequestMapping(value = "/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterRepository characterRepository;

    @GetMapping
    public String getBlank() {
        return "character";
    }

    @GetMapping(value = "/all")
    public @ResponseBody List<CharacterEntity> getAllCharacters() {
        return characterRepository.findAll();
    }

}
