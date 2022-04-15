package ru.poezdizm.timelinetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poezdizm.timelinetracker.entity.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Integer> {
}
