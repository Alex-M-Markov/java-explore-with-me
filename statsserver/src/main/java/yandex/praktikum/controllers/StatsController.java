package yandex.praktikum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yandex.praktikum.entities.dto.ViewStatsDto;
import yandex.praktikum.services.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/stats")
    public List<ViewStatsDto> get(
            @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(name = "uris") List<String> uris,
            @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        return statsService.get(start, end, uris, unique);
    }

}
