package yandex.praktikum.ewmservice.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.State;

import java.util.List;
import java.util.Optional;

public interface EventsRepository extends CrudRepository<Event, Long>, EventsCustomRepository {


    List<Event> findEventsByInitiator_Id(Long userId, Pageable pageable);

    Optional<Event> findByIdAndState(Long id, State state);

    List<Event> findEventsByIdIn(List<Long> ids);
}
