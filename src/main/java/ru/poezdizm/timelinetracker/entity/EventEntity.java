package ru.poezdizm.timelinetracker.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "events")
@Getter
@Setter
@RequiredArgsConstructor
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIME)
    private Date start;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIME)
    private Date end;

    @ManyToMany
    private List<CharacterEntity> characters;

    @ManyToMany
    private List<ChapterEntity> chapters;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventEntity eventEntity = (EventEntity) o;
        return id != null && Objects.equals(id, eventEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
