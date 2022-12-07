package yandex.praktikum.ewmservice.repositories;


import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.SortOptions;
import yandex.praktikum.ewmservice.entities.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsCustomRepository {
    List<Event> getEventsPublic(String annotation, List<Long> categories, Boolean paid,
                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                SortOptions sort, Integer from, Integer size);

    List<Event> getEventsAdmin(List<Long> users, List<State> states, List<Long> categories,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                               Integer from, Integer size);
}
