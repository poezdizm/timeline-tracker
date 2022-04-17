package ru.poezdizm.timelinetracker.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EventModel {

    private Integer id;

    private String content;

    private Date start;

    private Date end;

    private String title;

    private List<Integer> characterIds;

    private List<Integer> chapterIds;

    private String imageUrl;

}
