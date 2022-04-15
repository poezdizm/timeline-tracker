package ru.poezdizm.timelinetracker.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class EventModel {

    private Integer id;

    private String content;

    private Date start;

    private Date end;

}
