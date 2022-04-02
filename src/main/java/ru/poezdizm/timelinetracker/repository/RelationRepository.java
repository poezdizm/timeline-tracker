package ru.poezdizm.timelinetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poezdizm.timelinetracker.entity.RelationEntity;

@Repository
public interface RelationRepository extends JpaRepository<RelationEntity, Long> {
}
