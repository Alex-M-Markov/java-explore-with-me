package yandex.praktikum.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.entities.dto.ViewStatsDto;
import yandex.praktikum.entities.mappers.ViewStatsMapper;
import yandex.praktikum.repositories.HitsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StatsService {
    private final HitsRepository hitsRepository;

    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) {
            return hitsRepository.getStatsUnique(start, end, uris).stream()
                    .map(ViewStatsMapper::viewStatsToDto)
                    .collect(Collectors.toList());
        } else {
            return hitsRepository.getStats(start, end, uris).stream()
                    .map(ViewStatsMapper::viewStatsToDto)
                    .collect(Collectors.toList());
        }
    }

}
