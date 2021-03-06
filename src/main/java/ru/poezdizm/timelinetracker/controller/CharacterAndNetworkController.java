package ru.poezdizm.timelinetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.poezdizm.timelinetracker.model.CharacterModel;
import ru.poezdizm.timelinetracker.model.NetworkModel;
import ru.poezdizm.timelinetracker.request.CharacterRequest;
import ru.poezdizm.timelinetracker.service.CharacterService;
import ru.poezdizm.timelinetracker.service.NetworkService;

import java.util.List;

@Controller
@RequestMapping(value = "/characters")
@RequiredArgsConstructor
public class CharacterAndNetworkController {

    private final NetworkService networkService;
    private final CharacterService characterService;

    @GetMapping
    public String getBlank() {
        return "character";
    }

    @GetMapping(value = "/network")
    public @ResponseBody NetworkModel getAllCharacters(@RequestParam(required = false) Integer id) {
        if (id != null) {
            return networkService.getFilteredNetwork(id);
        }
        return networkService.getFullNetwork();
    }

    @GetMapping(value = "/all")
    public @ResponseBody List<CharacterModel> getAllCharacters() {
        return characterService.getAllCharacters();
    }

    @GetMapping(value = "/network/relation")
    public @ResponseBody NetworkModel getRelation(@RequestParam Long id) {
        if (id != null) {
            return networkService.getOneRelationNetwork(id);
        }
        return networkService.getFullNetwork();
    }

    @PostMapping(value = "/character")
    public @ResponseBody CharacterModel editCharacter(@RequestBody CharacterRequest request) {
        return characterService.updateCharacter(request);
    }

    @PostMapping(value = "/character/delete")
    public @ResponseBody CharacterModel deleteCharacter(@RequestParam Integer id) {
        characterService.deleteCharacter(id);
        return CharacterModel.builder().build();
    }

}
