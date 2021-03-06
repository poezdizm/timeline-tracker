package ru.poezdizm.timelinetracker.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "character_relations")
@Getter
@Setter
@RequiredArgsConstructor
public class RelationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_char")
    private Integer from;

    @Column(name = "to_char")
    private Integer to;

    @ManyToOne
    private RelationTypeEntity type;

    private Integer length;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RelationEntity relation = (RelationEntity) o;
        return id != null && Objects.equals(id, relation.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
