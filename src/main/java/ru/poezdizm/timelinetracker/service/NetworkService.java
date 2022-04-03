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
public class NetworkService {

    private final CharacterRepository characterRepository;
    private final RelationRepository relationRepository;
    private final RelationTypeRepository relationTypeRepository;

    private List<RelationEntity> relationCache;

    private static final String defaultColor = "#000000";
    private static final Integer defaultMainSize = 35;
    private static final Integer defaultOtherSize = 25;

    public NetworkModel getFullNetwork() {
        List<CharacterModel> characterModels = characterRepository.findAll().stream()
                .map(NetworkService::mapCharacter).toList();
        relationCache = relationRepository.findAll();
        List<RelationModel> relationModels = relationCache.stream().map(NetworkService::mapRelation).toList();
        return new NetworkModel(characterModels, relationModels);
    }

    public NetworkModel getFilteredNetwork(Integer id) {
        List<RelationModel> filteredRelations = new LinkedList<>();
        List<CharacterModel> filteredCharacters = new LinkedList<>();
        Optional<CharacterEntity> character = characterRepository.findById(id);
        if (character.isEmpty()) return new NetworkModel(filteredCharacters, filteredRelations);
        filteredCharacters.add(mapCharacter(character.get()));
        for (RelationEntity relationEntity : relationCache) {
            if (relationEntity.getFrom().equals(id) || relationEntity.getTo().equals(id)) {
                Optional<CharacterEntity> relatedCharacter = null;
                if (relationEntity.getFrom().equals(id)) {
                    relatedCharacter = characterRepository.findById(relationEntity.getTo());
                } else {
                    relatedCharacter = characterRepository.findById(relationEntity.getFrom());
                }
                if (relatedCharacter.isEmpty()) continue;
                filteredRelations.add(mapRelation(relationEntity));
                filteredCharacters.add(mapCharacter(relatedCharacter.get()));
            }
        }
        return new NetworkModel(filteredCharacters, filteredRelations);
    }

    public NetworkModel getOneRelationNetwork(Long id) {
        List<RelationModel> filteredRelations = relationCache.stream().filter(e -> e.getId().equals(id))
                .map(NetworkService::mapRelation).toList();
        if (filteredRelations.isEmpty()) {
            return null;
        }
        List<CharacterModel> filteredCharacters = new LinkedList<>();
        Optional<CharacterEntity> characterFrom = characterRepository.findById(filteredRelations.get(0).getFrom());
        Optional<CharacterEntity> characterTo = characterRepository.findById(filteredRelations.get(0).getTo());
        if (characterFrom.isEmpty() || characterTo.isEmpty()) {
            return null;
        }
        filteredCharacters.add(mapCharacter(characterFrom.get()));
        filteredCharacters.add(mapCharacter(characterTo.get()));
        return new NetworkModel(filteredCharacters, filteredRelations);
    }

    public List<CharacterModel> getAllCharacters() {
        return characterRepository.findAll().stream().map(NetworkService::mapCharacter).toList();
    }

    public CharacterModel updateCharacter(CharacterRequest request) {
        CharacterEntity entity = new CharacterEntity();
        if (request.getId() != null) {
            Optional<CharacterEntity> optional = characterRepository.findById(request.getId());
            if (optional.isPresent()) entity = optional.get();
        }
        entity.setName(request.getName());
        entity.setImageUrl(request.getImage());
        entity.setIsMain(request.getIsMain());
        entity.setIsDead(request.getIsDead());
        characterRepository.save(entity);
        return mapCharacter(entity);
    }

    public void deleteCharacter(Integer id) {
        characterRepository.deleteById(id);
    }

    public List<RelationTypeModel> getAllTypes() {
        return relationTypeRepository.findAll().stream().map(NetworkService::mapRelationType).toList();
    }

    public RelationModel updateRelation(RelationRequest request) {
        RelationEntity entity = new RelationEntity();
        if (request.getId() != null) {
            Optional<RelationEntity> optional = relationRepository.findById(request.getId());
            if (optional.isPresent()) entity = optional.get();
        }
        entity.setFrom(request.getFrom());
        entity.setTo(request.getTo());
        entity.setType(request.getType() != null ?
                relationTypeRepository.findById(request.getType()).orElse(null) : null);
        relationRepository.save(entity);
        return mapRelation(entity);
    }

    public RelationTypeModel createType(RelationTypeRequest request) {
        RelationTypeEntity entity = new RelationTypeEntity();
        entity.setLabel(request.getLabel());
        entity.setColor(request.getColor());
        relationTypeRepository.save(entity);
        return mapRelationType(entity);
    }

    public void deleteRelation(Long id) {
        relationRepository.deleteById(id);
    }

    private static CharacterModel mapCharacter(CharacterEntity entity) {
        return CharacterModel.builder().id(entity.getId())
                .label(entity.getIsDead() ? entity.getName() + "\uD83D\uDC80" : entity.getName())
                .image(entity.getImageUrl())
                .size(entity.getIsMain() ? defaultMainSize : defaultOtherSize)
                .build();
    }

    private static RelationModel mapRelation(RelationEntity entity) {
        if (entity.getType() == null) {
            entity.setType(new RelationTypeEntity(0, "", defaultColor));
        }
        return RelationModel.builder().id(entity.getId()).from(entity.getFrom()).to(entity.getTo())
                .label(entity.getType().getLabel()).color(entity.getType().getColor()).build();
    }

    private static RelationTypeModel mapRelationType(RelationTypeEntity entity) {
        return RelationTypeModel.builder().id(entity.getId()).label(entity.getLabel()).build();
    }

}
