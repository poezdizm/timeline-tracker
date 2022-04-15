package ru.poezdizm.timelinetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.poezdizm.timelinetracker.model.TimelineModel;
import ru.poezdizm.timelinetracker.repository.EventRepository;

@Service
@RequiredArgsConstructor
public class TimelineService {

    private final EventRepository eventRepository;

    public TimelineModel getTimeline() {
        return new TimelineModel(eventRepository.findAll().stream().map(EventService::mapEvent).toList());
    }

}
