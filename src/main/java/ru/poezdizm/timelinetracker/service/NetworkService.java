package ru.poezdizm.timelinetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.poezdizm.timelinetracker.entity.CharacterEntity;
import ru.poezdizm.timelinetracker.entity.RelationEntity;
import ru.poezdizm.timelinetracker.entity.RelationTypeEntity;
import ru.poezdizm.timelinetracker.model.CharacterModel;
import ru.poezdizm.timelinetracker.model.NetworkModel;
import ru.poezdizm.timelinetracker.model.RelationModel;
import ru.poezdizm.timelinetracker.repository.CharacterRepository;
import ru.poezdizm.timelinetracker.repository.RelationRepository;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NetworkService {

    private final CharacterRepository characterRepository;
    private final RelationRepository relationRepository;

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

}
