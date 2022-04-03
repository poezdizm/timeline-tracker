package ru.poezdizm.timelinetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.poezdizm.timelinetracker.model.RelationModel;
import ru.poezdizm.timelinetracker.model.RelationTypeModel;
import ru.poezdizm.timelinetracker.request.RelationRequest;
import ru.poezdizm.timelinetracker.request.RelationTypeRequest;
import ru.poezdizm.timelinetracker.service.NetworkService;

import java.util.List;

@Controller
@RequestMapping(value = "/relations")
@RequiredArgsConstructor
public class RelationController {

    private final NetworkService networkService;

    @GetMapping(value = "/types")
    public @ResponseBody List<RelationTypeModel> getAllTypes() {
        return networkService.getAllTypes();
    }

    @PostMapping
    public @ResponseBody RelationModel editRelation(@RequestBody RelationRequest request) {
        return networkService.updateRelation(request);
    }

    @PostMapping(value = "/types")
    public @ResponseBody RelationTypeModel createType(@RequestBody RelationTypeRequest request) {
        return networkService.createType(request);
    }

    @PostMapping(value = "/delete")
    public @ResponseBody String deleteRelation(@RequestParam Long id) {
        networkService.deleteRelation(id);
        return "delete";
    }

}
