package ru.poezdizm.timelinetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.poezdizm.timelinetracker.entity.CharacterEntity;
import ru.poezdizm.timelinetracker.model.*;
import ru.poezdizm.timelinetracker.repository.CharacterRepository;
import ru.poezdizm.timelinetracker.request.CharacterRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final ValidationService validationService;

    private static final Integer DEFAULT_MAIN_SIZE = 35;
    private static final Integer DEFAULT_OTHER_SIZE = 25;

    public List<CharacterModel> getAllCharacters() {
        return characterRepository.findAll().stream().map(CharacterService::mapCharacter).toList();
    }

    public CharacterModel updateCharacter(CharacterRequest request) {
        if (Boolean.FALSE.equals(validationService.validateCharacter(request))) {
            return null;
        }
        CharacterEntity entity = new CharacterEntity();
        if (request.getId() != null) {
            Optional<CharacterEntity> optional = characterRepository.findById(request.getId());
            if (optional.isPresent()) entity = optional.get();
        }
        entity.setName(request.getName().trim());
        entity.setImageUrl(request.getImage());
        entity.setIsMain(request.getIsMain());
        entity.setIsDead(request.getIsDead());
        characterRepository.save(entity);
        return mapCharacter(entity);
    }

    public void deleteCharacter(Integer id) {
        characterRepository.deleteById(id);
    }

    public static CharacterModel mapCharacter(CharacterEntity entity) {
        return CharacterModel.builder().id(entity.getId())
                .label(Boolean.TRUE.equals(entity.getIsDead()) ? entity.getName() + "\uD83D\uDC80" : entity.getName())
                .image(entity.getImageUrl())
                .size(Boolean.TRUE.equals(entity.getIsMain()) ? DEFAULT_MAIN_SIZE : DEFAULT_OTHER_SIZE)
                .build();
    }

}
