package ru.poezdizm.timelinetracker.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RelationRequest {

    private Long id;

    private Integer from;

    private Integer to;

    private Integer type;

}
