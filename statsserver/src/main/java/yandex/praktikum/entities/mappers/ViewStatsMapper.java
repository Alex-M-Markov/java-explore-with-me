package yandex.praktikum.entities.mappers;

import lombok.experimental.UtilityClass;
import yandex.praktikum.entities.ViewStats;
import yandex.praktikum.entities.dto.ViewStatsDto;

@UtilityClass
public class ViewStatsMapper {
    public static ViewStatsDto viewStatsToDto(ViewStats viewStats) {
        return new ViewStatsDto(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
    }
}
