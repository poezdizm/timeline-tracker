package ru.poezdizm.timelinetracker.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "characters")
@Getter
@Setter
@RequiredArgsConstructor
public class CharacterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "main")
    private Boolean isMain;

    @Column(name = "dead")
    private Boolean isDead;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "from")
    private List<RelationEntity> relations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CharacterEntity character = (CharacterEntity) o;
        return id != null && Objects.equals(id, character.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
