package ru.poezdizm.timelinetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.poezdizm.timelinetracker.repository.RelationRepository;
import ru.poezdizm.timelinetracker.request.*;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final RelationRepository relationRepository;

    public Boolean validateCharacter(CharacterRequest request) {
        return request.getName() != null && !request.getName().trim().isEmpty() &&
                request.getName().trim().matches("[a-zA-Zа-яА-Я0-9 ]+");
    }

    public Boolean validateRelation(RelationRequest request) {
        if (request.getFrom().equals(request.getTo())) {
            return false;
        }
        if (request.getLength() == null) {
            request.setLength(200);
        } else if (request.getLength() < 100 || request.getLength() > 500) {
            return false;
        }
        return request.getId() != null || relationRepository.findAll().stream()
                .noneMatch(e -> (e.getFrom().equals(request.getFrom()) && e.getTo().equals(request.getTo())) ||
                        (e.getFrom().equals(request.getTo()) && e.getTo().equals(request.getFrom())));
    }

    public Boolean validateRelationType(RelationTypeRequest request) {
        return request.getLabel() != null && !request.getLabel().trim().isEmpty() &&
                request.getLabel().trim().matches("[\"a-zA-Zа-яА-Я0-9 -]+") &&
                request.getColor().matches("#[A-F0-9]{6}");
    }

    public Boolean validateEvent(EventRequest request) {
        return request.getTitle() != null && !request.getTitle().trim().isEmpty() &&
                request.getTitle().trim().matches("[\"a-zA-Zа-яА-Я0-9 .,-]+") &&
                request.getStart() != null && request.getStart().trim().matches("^\\d{2}.\\d{2}.\\d{4}$") &&
                (request.getEnd() == null || request.getEnd().isEmpty() || request.getEnd().trim().matches("^\\d{2}.\\d{2}.\\d{4}$")) &&
                !request.getCharacters().contains(null) && !request.getChapters().contains(null);
    }

    public Boolean validateChapter(ChapterRequest request) {
        return request.getTitle() != null && !request.getTitle().trim().isEmpty() &&
                request.getTitle().trim().matches("[\"a-zA-Zа-яА-Я0-9 .,-]+");
    }

}
