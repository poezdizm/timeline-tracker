package ru.poezdizm.timelinetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.poezdizm.timelinetracker.model.NetworkModel;
import ru.poezdizm.timelinetracker.request.CharacterRequest;
import ru.poezdizm.timelinetracker.service.NetworkService;

@Controller
@RequestMapping(value = "/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final NetworkService networkService;

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

    @GetMapping(value = "/network/relation")
    public @ResponseBody NetworkModel getRelation(@RequestParam Long id) {
        if (id != null) {
            return networkService.getOneRelationNetwork(id);
        }
        return networkService.getFullNetwork();
    }

    @PostMapping(value = "/character")
    public @ResponseBody NetworkModel editCharacter(@RequestBody CharacterRequest request) {
        return networkService.updateCharacter(request);
    }

}
