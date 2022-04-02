package ru.poezdizm.timelinetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.poezdizm.timelinetracker.entity.CharacterEntity;
import ru.poezdizm.timelinetracker.entity.RelationEntity;
import ru.poezdizm.timelinetracker.model.CharacterModel;
import ru.poezdizm.timelinetracker.model.NetworkModel;
import ru.poezdizm.timelinetracker.model.RelationModel;
import ru.poezdizm.timelinetracker.repository.CharacterRepository;
import ru.poezdizm.timelinetracker.repository.RelationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NetworkService {

    private final CharacterRepository characterRepository;
    private final RelationRepository relationRepository;

    private List<CharacterEntity> characterCache;
    private List<RelationEntity> relationCache;

    public NetworkModel getFullNetwork() {
        characterCache = characterRepository.findAll();
        List<CharacterModel> characterModels = characterCache.stream().map(NetworkService::mapCharacter).toList();
        relationCache = relationRepository.findAll();
        List<RelationModel> relationModels = relationCache.stream().map(NetworkService::mapRelation).toList();
        return new NetworkModel(characterModels, relationModels);
    }

    public NetworkModel getFilteredNetwork(Integer id) {
        List<CharacterModel> filteredCharacters = characterCache.stream()
                .filter(e -> e.getId().equals(id) || e.getRelations().stream().anyMatch(r -> r.getTo().equals(id)))
                .map(NetworkService::mapCharacter).toList();
        List<RelationModel> filteredRelations = relationCache.stream().filter(e -> e.getFrom().equals(id))
                .map(NetworkService::mapRelation).toList();
        return new NetworkModel(filteredCharacters, filteredRelations);
    }

    private static CharacterModel mapCharacter(CharacterEntity entity) {
        return CharacterModel.builder().id(entity.getId()).name(entity.getName()).imageUrl(entity.getImageUrl()).build();
    }

    private static RelationModel mapRelation(RelationEntity entity) {
        return RelationModel.builder().from(entity.getFrom()).to(entity.getTo()).build();
    }

}
