package ru.poezdizm.timelinetracker.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChapterRequest {

    private Integer id;

    private String title;

}
