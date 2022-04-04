package ru.poezdizm.timelinetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.poezdizm.timelinetracker.model.RelationModel;
import ru.poezdizm.timelinetracker.model.RelationTypeFullModel;
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

    @GetMapping(value = "/type")
    public @ResponseBody RelationTypeFullModel getTypeByRelationId(@RequestParam Long relationId) {
        return networkService.getTypeByRelationId(relationId);
    }

    @PostMapping
    public @ResponseBody RelationModel editRelation(@RequestBody RelationRequest request) {
        return networkService.updateRelation(request);
    }

    @PostMapping(value = "/types")
    public @ResponseBody RelationTypeModel editType(@RequestBody RelationTypeRequest request) {
        return networkService.editType(request);
    }

    @PostMapping(value = "/delete")
    public @ResponseBody RelationModel deleteRelation(@RequestParam Long id) {
        networkService.deleteRelation(id);
        return RelationModel.builder().build();
    }

}
