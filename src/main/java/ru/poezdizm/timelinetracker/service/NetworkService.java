package ru.poezdizm.timelinetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.poezdizm.timelinetracker.entity.CharacterEntity;
import ru.poezdizm.timelinetracker.entity.RelationEntity;
import ru.poezdizm.timelinetracker.model.*;
import ru.poezdizm.timelinetracker.repository.CharacterRepository;
import ru.poezdizm.timelinetracker.repository.RelationRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NetworkService {

    private final CharacterRepository characterRepository;
    private final RelationRepository relationRepository;

    private List<RelationEntity> relationCache;

    public NetworkModel getFullNetwork() {
        List<CharacterModel> characterModels = characterRepository.findAll().stream()
                .map(CharacterService::mapCharacter).toList();
        relationCache = relationRepository.findAll();
        List<RelationModel> relationModels = relationCache.stream().map(RelationService::mapRelation).toList();
        return new NetworkModel(characterModels, relationModels);
    }

    public NetworkModel getFilteredNetwork(Integer id) {
        List<RelationModel> filteredRelations = new LinkedList<>();
        List<CharacterModel> filteredCharacters = new LinkedList<>();
        Optional<CharacterEntity> character = characterRepository.findById(id);
        if (character.isEmpty()) return new NetworkModel(filteredCharacters, filteredRelations);
        filteredCharacters.add(CharacterService.mapCharacter(character.get()));
        for (RelationEntity relationEntity : relationCache) {
            if (relationEntity.getFrom().equals(id) || relationEntity.getTo().equals(id)) {
                Optional<CharacterEntity> relatedCharacter;
                if (relationEntity.getFrom().equals(id)) {
                    relatedCharacter = characterRepository.findById(relationEntity.getTo());
                } else {
                    relatedCharacter = characterRepository.findById(relationEntity.getFrom());
                }
                if (relatedCharacter.isEmpty()) continue;
                filteredRelations.add(RelationService.mapRelation(relationEntity));
                filteredCharacters.add(CharacterService.mapCharacter(relatedCharacter.get()));
            }
        }
        return new NetworkModel(filteredCharacters, filteredRelations);
    }

    public NetworkModel getOneRelationNetwork(Long id) {
        List<RelationModel> filteredRelations = relationCache.stream().filter(e -> e.getId().equals(id))
                .map(RelationService::mapRelation).toList();
        if (filteredRelations.isEmpty()) {
            return null;
        }
        List<CharacterModel> filteredCharacters = new LinkedList<>();
        Optional<CharacterEntity> characterFrom = characterRepository.findById(filteredRelations.get(0).getFrom());
        Optional<CharacterEntity> characterTo = characterRepository.findById(filteredRelations.get(0).getTo());
        if (characterFrom.isEmpty() || characterTo.isEmpty()) {
            return null;
        }
        filteredCharacters.add(CharacterService.mapCharacter(characterFrom.get()));
        filteredCharacters.add(CharacterService.mapCharacter(characterTo.get()));
        return new NetworkModel(filteredCharacters, filteredRelations);
    }

}
