package ru.poezdizm.timelinetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poezdizm.timelinetracker.entity.ChapterEntity;

@Repository
public interface ChapterRepository extends JpaRepository<ChapterEntity, Integer> {
}
