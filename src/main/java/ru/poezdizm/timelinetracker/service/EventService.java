package ru.poezdizm.timelinetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.poezdizm.timelinetracker.entity.ChapterEntity;
import ru.poezdizm.timelinetracker.entity.CharacterEntity;
import ru.poezdizm.timelinetracker.entity.EventEntity;
import ru.poezdizm.timelinetracker.model.EventModel;
import ru.poezdizm.timelinetracker.repository.ChapterRepository;
import ru.poezdizm.timelinetracker.repository.CharacterRepository;
import ru.poezdizm.timelinetracker.repository.EventRepository;
import ru.poezdizm.timelinetracker.request.EventRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final CharacterRepository characterRepository;
    private final ChapterRepository chapterRepository;
    private final ValidationService validationService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private static final String CONTENT_TEMPLATE = "<h4>%s</h4>";
    private static final String CONTENT_TEMPLATE_CHAPTERS = "<p>Главы: %s</p>";
    private static final String CONTENT_TEMPLATE_CHARACTERS = "<p>Участники: %s</p>";
    private static final String CONTENT_TEMPLATE_IMAGE = "<img height=\"100px\" width=\"100px\" src=\"%s\">";

    public EventModel updateEvent(EventRequest request) {
        if (Boolean.FALSE.equals(validationService.validateEvent(request))) {
            return null;
        }
        EventEntity entity = new EventEntity();
        if (request.getId() != null) {
            Optional<EventEntity> optional = eventRepository.findById(request.getId());
            if (optional.isPresent()) entity = optional.get();
        }
        entity.setTitle(request.getTitle().trim());
        entity.setImageUrl(request.getImage());

        List<CharacterEntity> characters = new LinkedList<>();
        for (Integer characterId : request.getCharacters()) {
            Optional<CharacterEntity> optional = characterRepository.findById(characterId);
            optional.ifPresent(characters::add);
        }
        entity.setCharacters(characters);

        List<ChapterEntity> chapters = new LinkedList<>();
        for (Integer chapterId : request.getChapters()) {
            Optional<ChapterEntity> optional = chapterRepository.findById(chapterId);
            optional.ifPresent(chapters::add);
        }
        entity.setChapters(chapters);

        entity.setStart(parseDate(request.getStart()));
        entity.setEnd(request.getEnd() == null || request.getEnd().isEmpty() ? null : parseDate(request.getEnd()));

        eventRepository.save(entity);
        return mapEvent(entity);
    }

    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }

    public static EventModel mapEvent(EventEntity entity) {
        EventModel event = new EventModel();
        event.setId(entity.getId());
        event.setStart(entity.getStart());
        event.setEnd(entity.getEnd());

        String characters = entity.getCharacters().stream().map(CharacterEntity::getName).collect(Collectors.joining(", "));
        String chapters = entity.getChapters().stream().map(ChapterEntity::getTitle).collect(Collectors.joining(", "));

        String content = String.format(CONTENT_TEMPLATE, entity.getTitle());
        if (!characters.isEmpty()) {
            content += CONTENT_TEMPLATE_CHARACTERS;
            content = String.format(content, characters);
        }
        if (!chapters.isEmpty()) {
            content += CONTENT_TEMPLATE_CHAPTERS;
            content = String.format(content, chapters);
        }
        if (entity.getImageUrl() != null && !entity.getImageUrl().isEmpty()) {
            content += CONTENT_TEMPLATE_IMAGE;
            content = String.format(content, entity.getImageUrl());
        }
        event.setContent(content);

        return event;
    }

    private Date parseDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
