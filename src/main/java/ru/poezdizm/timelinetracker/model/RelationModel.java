package ru.poezdizm.timelinetracker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RelationModel {

    private Long id;

    private Integer from;

    private Integer to;

    private String label;

    private String color;

}
