package yandex.praktikum.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface HitsRepository extends JpaRepository<yandex.praktikum.entities.EndpointHit, Long> {
    @Query("SELECT new yandex.praktikum.entities.ViewStats(e.app, e.uri, COUNT (e.ip)) from EndpointHit e WHERE e.timestamp BETWEEN :start AND :end AND e.uri IN :uris GROUP BY e.app, e.uri")
    List<yandex.praktikum.entities.ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new yandex.praktikum.entities.ViewStats(e.app, e.uri, COUNT (DISTINCT e.ip)) from EndpointHit e WHERE e.timestamp BETWEEN :start AND :end AND e.uri IN :uris GROUP BY e.app, e.uri")
    List<yandex.praktikum.entities.ViewStats> getStatsUnique(LocalDateTime start, LocalDateTime end, List<String> uris);


}