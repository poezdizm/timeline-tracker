package ru.poezdizm.timelinetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.poezdizm.timelinetracker.entity.ChapterEntity;
import ru.poezdizm.timelinetracker.entity.CharacterEntity;
import ru.poezdizm.timelinetracker.entity.EventEntity;
import ru.poezdizm.timelinetracker.model.EventModel;
import ru.poezdizm.timelinetracker.model.TimelineModel;
import ru.poezdizm.timelinetracker.repository.EventRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimelineService {

    private final EventRepository eventRepository;

    private static final String CONTENT_TEMPLATE = "<h4>%s</h4>" +
                                                    "<p>Участники: %s</p>" +
                                                    "<p>Главы: %s</p>" +
                                                    "<img src=\"%s\">";

    public TimelineModel getTimeline() {
        return new TimelineModel(eventRepository.findAll().stream().map(TimelineService::mapEvent).toList());
    }

    public static EventModel mapEvent(EventEntity entity) {
        EventModel event = new EventModel();
        event.setId(entity.getId());
        event.setStart(entity.getStart());
        event.setEnd(entity.getEnd());

        String characters = entity.getCharacters().stream().map(CharacterEntity::getName).collect(Collectors.joining(", "));
        String chapters = entity.getChapters().stream().map(ChapterEntity::getTitle).collect(Collectors.joining(", "));

        String content = String.format(CONTENT_TEMPLATE, entity.getTitle(), characters, chapters, entity.getImageUrl());
        event.setContent(content);

        return event;
    }

}
