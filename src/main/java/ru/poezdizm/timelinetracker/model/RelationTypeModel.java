package ru.poezdizm.timelinetracker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RelationTypeModel {

    private Integer id;

    private String label;

}
