package ru.poezdizm.timelinetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.poezdizm.timelinetracker.model.EventModel;
import ru.poezdizm.timelinetracker.request.EventRequest;
import ru.poezdizm.timelinetracker.service.EventService;

@Controller
@RequestMapping(value = "/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public @ResponseBody EventModel editEvent(@RequestBody EventRequest eventRequest) {
        return eventService.updateEvent(eventRequest);
    }

}
