package ru.poezdizm.timelinetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.poezdizm.timelinetracker.model.NetworkModel;
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

}
