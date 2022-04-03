package ru.poezdizm.timelinetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poezdizm.timelinetracker.entity.RelationTypeEntity;

@Repository
public interface RelationTypeRepository extends JpaRepository<RelationTypeEntity, Integer> {
}
