package ru.poezdizm.timelinetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.poezdizm.timelinetracker.entity.RelationEntity;
import ru.poezdizm.timelinetracker.entity.RelationTypeEntity;
import ru.poezdizm.timelinetracker.model.*;
import ru.poezdizm.timelinetracker.repository.RelationRepository;
import ru.poezdizm.timelinetracker.repository.RelationTypeRepository;
import ru.poezdizm.timelinetracker.request.RelationRequest;
import ru.poezdizm.timelinetracker.request.RelationTypeRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RelationService {

    private final RelationRepository relationRepository;
    private final RelationTypeRepository relationTypeRepository;
    private final ValidationService validationService;

    private static final String DEFAULT_COLOR = "#000000";

    public List<RelationTypeModel> getAllTypes() {
        return relationTypeRepository.findAll().stream().map(RelationService::mapRelationType).toList();
    }

    public RelationTypeFullModel getTypeByRelationId(Long relationId) {
        Optional<RelationEntity> relation = relationRepository.findById(relationId);
        if (relation.isEmpty()) return null;
        Optional<RelationTypeEntity> type = relationTypeRepository.findById(relation.get().getType().getId());
        if (type.isEmpty()) return null;
        return mapFullRelationType(type.get());
    }

    public RelationModel updateRelation(RelationRequest request) {
        if (Boolean.FALSE.equals(validationService.validateRelation(request))) {
            return null;
        }
        RelationEntity entity = new RelationEntity();
        if (request.getId() != null) {
            Optional<RelationEntity> optional = relationRepository.findById(request.getId());
            if (optional.isPresent()) entity = optional.get();
        }
        entity.setFrom(request.getFrom());
        entity.setTo(request.getTo());
        entity.setType(request.getType() != null ?
                relationTypeRepository.findById(request.getType()).orElse(null) : null);
        entity.setLength(request.getLength());
        relationRepository.save(entity);
        return mapRelation(entity);
    }

    public RelationTypeModel editType(RelationTypeRequest request) {
        if (Boolean.FALSE.equals(validationService.validateRelationType(request))) {
            return null;
        }
        RelationTypeEntity entity = new RelationTypeEntity();
        if (request.getId() != null) {
            Optional<RelationTypeEntity> optional = relationTypeRepository.findById(request.getId());
            if (optional.isPresent()) entity = optional.get();
        }
        entity.setLabel(request.getLabel());
        entity.setColor(request.getColor());
        entity.setHasPointer(request.getHasPointer());
        entity.setDashes(request.getDashes());
        relationTypeRepository.save(entity);
        return mapRelationType(entity);
    }

    public void deleteRelation(Long id) {
        relationRepository.deleteById(id);
    }

    public static RelationModel mapRelation(RelationEntity entity) {
        if (entity.getType() == null) {
            entity.setType(new RelationTypeEntity(0, "", DEFAULT_COLOR, false, false));
        }
        return RelationModel.builder().id(entity.getId()).from(entity.getFrom()).to(entity.getTo())
                .label(entity.getType().getLabel()).color(entity.getType().getColor())
                .hasPointer(entity.getType().getHasPointer()).dashes(entity.getType().getDashes())
                .length(entity.getLength()).build();
    }

    private static RelationTypeModel mapRelationType(RelationTypeEntity entity) {
        return RelationTypeModel.builder().id(entity.getId()).label(entity.getLabel()).build();
    }

    private static RelationTypeFullModel mapFullRelationType(RelationTypeEntity entity) {
        return RelationTypeFullModel.builder().id(entity.getId()).label(entity.getLabel())
                .color(entity.getColor()).hasPointer(entity.getHasPointer()).dashes(entity.getDashes()).build();
    }

}
