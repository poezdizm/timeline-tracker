package ru.poezdizm.timelinetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.poezdizm.timelinetracker.model.ChapterModel;
import ru.poezdizm.timelinetracker.request.ChapterRequest;
import ru.poezdizm.timelinetracker.service.ChapterService;

import java.util.List;

@Controller
@RequestMapping(value = "/chapter")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @GetMapping
    public @ResponseBody List<ChapterModel> getAllChapters() {
        return chapterService.getAllChapters();
    }

    @PostMapping
    public @ResponseBody ChapterModel editChapter(@RequestBody ChapterRequest chapterRequest) {
        return chapterService.updateChapter(chapterRequest);
    }

}
