package yandex.praktikum.ewmservice.services.privateuser;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.ParticipationRequest;
import yandex.praktikum.ewmservice.entities.ParticipationStatus;
import yandex.praktikum.ewmservice.entities.State;
import yandex.praktikum.ewmservice.entities.dto.request.ParticipationRequestDto;
import yandex.praktikum.ewmservice.entities.mappers.RequestMapper;
import yandex.praktikum.ewmservice.exceptions.*;
import yandex.praktikum.ewmservice.repositories.EventsRepository;
import yandex.praktikum.ewmservice.repositories.RequestsRepository;
import yandex.praktikum.ewmservice.repositories.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestsService {

    private final RequestsRepository requestsRepository;
    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public ParticipationRequestDto post(Long userId, Long eventId) {
        if (requestsRepository.findByRequester_IdAndEvent_Id(userId, eventId).isPresent()) {
            throw new DuplicateRequestException("Такой запрос уже был отправлен");
        }
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (userId.equals(event.getInitiator().getId())) {
            throw new SameUserEventException();
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EventStateException();
        }
        if (getForUserEvent(userId, eventId).size() >= event.getParticipantLimit()) {
            throw new EventIsFullException();
        }

        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .withCreated(LocalDateTime.now())
                .withEvent(event)
                .withRequester(usersRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)))
                .withStatus(ParticipationStatus.PENDING)
                .build();
        if (!event.getRequestModeration()) {
            participationRequest.setStatus(ParticipationStatus.CONFIRMED);
        }
        requestsRepository.save(participationRequest);
        return RequestMapper.requestToDto(participationRequest);
    }

    public List<ParticipationRequestDto> getAll(Long userId) {
        log.info("Получаем все заявки пользователя {} на участие в событиях", userId);
        return requestsRepository.findAllByRequester_Id(userId)
                .stream()
                .map(RequestMapper::requestToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        ParticipationRequest participationRequest = requestsRepository.findByIdAndRequester_Id(requestId, userId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
        participationRequest.setStatus(ParticipationStatus.CANCELED);
                return RequestMapper.requestToDto(participationRequest);
    }

    public List<ParticipationRequestDto> getForUserEvent(Long userId, Long eventId) {
        return requestsRepository.findAllByEvent_Initiator_IdAndEvent_Id(userId, eventId)
                .stream()
                .map(RequestMapper::requestToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto confirmForUserEvent(Long userId, Long eventId, Long reqId) {
        ParticipationRequest participationRequest = requestsRepository.findById(reqId)
                .orElseThrow(() -> new RequestNotFoundException(reqId));
        Event event = participationRequest.getEvent();
        if (!event.getInitiator().getId().equals(userId)) {
            throw new WrongUserException();
        }
        if (getForUserEvent(userId, eventId).size() >= event.getParticipantLimit()) {
            throw new EventIsFullException();
        }
        if (getForUserEvent(userId, eventId).size() == (event.getParticipantLimit())) {
            List<ParticipationRequest> requestsToCancel = requestsRepository.findAllByEvent_IdAndStatus(eventId,
                    ParticipationStatus.PENDING);
            for (ParticipationRequest request : requestsToCancel) {
                request.setStatus(ParticipationStatus.CANCELED);
            }
        }
        participationRequest.setStatus(ParticipationStatus.CONFIRMED);
        return RequestMapper.requestToDto(participationRequest);
    }

    @Transactional
    public ParticipationRequestDto rejectForUserEvent(Long userId, Long eventId, Long reqId) {
        ParticipationRequest participationRequest = requestsRepository
                .findById(reqId).orElseThrow(() -> new RequestNotFoundException(reqId));
        Event event = participationRequest.getEvent();
        if (!event.getInitiator().getId().equals(userId)) {
            throw new WrongUserException();
        }
        participationRequest.setStatus(ParticipationStatus.REJECTED);
        return RequestMapper.requestToDto(participationRequest);
    }

    @Transactional
    public long countConfirmedRequests(Long eventId) {
        List<ParticipationRequest> requests = requestsRepository.findAllByEvent_Id(eventId);
        if (requests == null) {
            return 0L;
        } else {
            return requests.size();
        }
    }

}
