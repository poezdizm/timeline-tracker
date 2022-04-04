package ru.poezdizm.timelinetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.poezdizm.timelinetracker.entity.CharacterEntity;
import ru.poezdizm.timelinetracker.entity.RelationEntity;
import ru.poezdizm.timelinetracker.entity.RelationTypeEntity;
import ru.poezdizm.timelinetracker.model.CharacterModel;
import ru.poezdizm.timelinetracker.model.NetworkModel;
import ru.poezdizm.timelinetracker.model.RelationModel;
import ru.poezdizm.timelinetracker.model.RelationTypeModel;
import ru.poezdizm.timelinetracker.repository.CharacterRepository;
import ru.poezdizm.timelinetracker.repository.RelationRepository;
import ru.poezdizm.timelinetracker.repository.RelationTypeRepository;
import ru.poezdizm.timelinetracker.request.CharacterRequest;
import ru.poezdizm.timelinetracker.request.RelationRequest;
import ru.poezdizm.timelinetracker.request.RelationTypeRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final RelationRepository relationRepository;

    public Boolean validateCharacter(CharacterRequest request) {
        return request.getName().trim().matches("[a-zA-Zа-яА-Я0-9 ]+") &&
                request.getName() != null && !request.getName().trim().isEmpty();
    }

    public Boolean validateRelation(RelationRequest request) {
        if (request.getFrom().equals(request.getTo())) {
            return false;
        }
        return request.getId() != null || !relationRepository.findAll().stream()
                .anyMatch(e -> (e.getFrom().equals(request.getFrom()) && e.getTo().equals(request.getTo())) ||
                        (e.getFrom().equals(request.getTo()) && e.getTo().equals(request.getFrom())));
    }

    public Boolean validateRelationType(RelationTypeRequest request) {
        return request.getLabel().trim().matches("[a-zA-Zа-яА-Я0-9 ]+") &&
                request.getLabel() != null && !request.getLabel().trim().isEmpty() &&
                request.getColor().matches("#[A-F0-9]{6}");
    }

}
