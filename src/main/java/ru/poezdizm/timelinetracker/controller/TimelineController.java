package ru.poezdizm.timelinetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.poezdizm.timelinetracker.model.TimelineModel;
import ru.poezdizm.timelinetracker.service.TimelineService;

@Controller
@RequestMapping(value = "/timeline")
@RequiredArgsConstructor
public class TimelineController {

    private final TimelineService timelineService;

    @GetMapping
    public String getBlank() {
        return "timeline";
    }

    @GetMapping(value = "/data")
    public @ResponseBody TimelineModel getTimeline() {
        return timelineService.getTimeline();
    }

}
