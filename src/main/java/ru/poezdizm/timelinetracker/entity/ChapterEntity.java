package ru.poezdizm.timelinetracker.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "chapters")
@Getter
@Setter
@RequiredArgsConstructor
public class ChapterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChapterEntity chapterEntity = (ChapterEntity) o;
        return id != null && Objects.equals(id, chapterEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
