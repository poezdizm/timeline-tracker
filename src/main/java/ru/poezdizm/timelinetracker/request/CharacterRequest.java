package ru.poezdizm.timelinetracker.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CharacterRequest {

    private Integer id;

    private String name;

    private String image;

    private Boolean isMain;

    private Boolean isDead;

}
