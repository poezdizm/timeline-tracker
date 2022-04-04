package ru.poezdizm.timelinetracker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RelationTypeFullModel {

    private Integer id;

    private String label;

    private String color;

    private Boolean hasPointer;

    private Boolean dashes;

}
