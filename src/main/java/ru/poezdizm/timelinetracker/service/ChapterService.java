package ru.poezdizm.timelinetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.poezdizm.timelinetracker.entity.ChapterEntity;
import ru.poezdizm.timelinetracker.model.ChapterModel;
import ru.poezdizm.timelinetracker.repository.ChapterRepository;
import ru.poezdizm.timelinetracker.request.ChapterRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final ValidationService validationService;

    public List<ChapterModel> getAllChapters() {
        return chapterRepository.findAll().stream().map(ChapterService::mapChapter).toList();
    }

    public ChapterModel updateChapter(ChapterRequest request) {
        if (Boolean.FALSE.equals(validationService.validateChapter(request))) {
            return null;
        }
        ChapterEntity entity = new ChapterEntity();
        if (request.getId() != null) {
            Optional<ChapterEntity> optional = chapterRepository.findById(request.getId());
            if (optional.isPresent()) entity = optional.get();
        }
        entity.setTitle(request.getTitle().trim());

        chapterRepository.save(entity);
        return mapChapter(entity);
    }

    public void deleteChapter(Integer id) {
        chapterRepository.deleteById(id);
    }

    public static ChapterModel mapChapter(ChapterEntity entity) {
        return ChapterModel.builder().id(entity.getId()).title(entity.getTitle()).build();
    }
}
