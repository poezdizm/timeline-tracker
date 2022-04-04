package ru.poezdizm.timelinetracker.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "relation_types")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class RelationTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String label;

    private String color;

    @Column(name = "pointer")
    private Boolean hasPointer;

    private Boolean dashes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RelationTypeEntity type = (RelationTypeEntity) o;
        return id != null && Objects.equals(id, type.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
