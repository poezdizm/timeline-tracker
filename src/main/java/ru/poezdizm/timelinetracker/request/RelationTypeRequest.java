package ru.poezdizm.timelinetracker.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RelationTypeRequest {

    private Integer id;

    private String label;

    private String color;

    private Boolean hasPointer;

    private Boolean dashes;

}
