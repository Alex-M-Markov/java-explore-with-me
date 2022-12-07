package yandex.praktikum.ewmservice.repositories;


import org.springframework.data.repository.CrudRepository;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.ParticipationRequest;
import yandex.praktikum.ewmservice.entities.ParticipationStatus;

import java.util.List;
import java.util.Optional;

public interface RequestsRepository extends CrudRepository<ParticipationRequest, Long> {

    Optional<ParticipationRequest> findByRequester_IdAndEvent_Id(Long requesterId, Long eventId);

    List<ParticipationRequest> findAllByRequester_Id(Long requesterId);

    List<ParticipationRequest> findAllByEvent_Id(Long eventId);

    List<ParticipationRequest> findParticipationRequestsByEventIn(List<Event> events);

    List<ParticipationRequest> findAllByEvent_IdAndStatus(Long eventId, ParticipationStatus participationStatus);

    Optional<ParticipationRequest> findByIdAndRequester_Id(Long id, Long requesterId);
}
