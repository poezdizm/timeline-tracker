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
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date end;

    @ManyToMany
    @JoinTable(
            name = "events_characters",
            joinColumns = { @JoinColumn(name = "event_id") },
            inverseJoinColumns = { @JoinColumn(name = "character_id") }
    )
    private List<CharacterEntity> characters;

    @ManyToMany
    @JoinTable(
            name = "events_chapters",
            joinColumns = { @JoinColumn(name = "event_id") },
            inverseJoinColumns = { @JoinColumn(name = "chapter_id") }
    )
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
