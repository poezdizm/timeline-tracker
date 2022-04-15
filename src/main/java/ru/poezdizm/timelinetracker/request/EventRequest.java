package ru.poezdizm.timelinetracker.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class EventRequest {

    private Integer id;

    private String title;

    private String image;

    private String start;

    private String end;

    private List<Integer> characters;

    private List<Integer> chapters;

}
