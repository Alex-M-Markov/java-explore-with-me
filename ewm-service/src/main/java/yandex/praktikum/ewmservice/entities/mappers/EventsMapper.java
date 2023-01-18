package yandex.praktikum.ewmservice.entities.mappers;


import lombok.experimental.UtilityClass;
import yandex.praktikum.ewmservice.entities.Category;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.State;
import yandex.praktikum.ewmservice.entities.User;
import yandex.praktikum.ewmservice.entities.dto.event.EventFullDto;
import yandex.praktikum.ewmservice.entities.dto.event.EventFullDtoWithComments;
import yandex.praktikum.ewmservice.entities.dto.event.EventShortDto;
import yandex.praktikum.ewmservice.entities.dto.event.NewEventDto;

import java.time.LocalDateTime;

@UtilityClass
public class EventsMapper {

    public static Event fromNewEventDto(NewEventDto newEventDto, Category category, User user, LocalDateTime createdOn,
                                        LocalDateTime publishedOn) {
        return Event.builder()
                .withTitle(newEventDto.getTitle())
                .withAnnotation(newEventDto.getAnnotation())
                .withDescription(newEventDto.getDescription())
                .withCategory(category)
                .withEventDate(newEventDto.getEventDate())
                .withLocation(newEventDto.getLocation())
                .withPaid(newEventDto.getPaid())
                .withParticipantLimit(newEventDto.getParticipantLimit())
                .withRequestModeration(newEventDto.getRequestModeration())
                .withInitiator(user)
                .withState(State.PENDING)
                .withCreatedOn(createdOn)
                .withPublishedOn(publishedOn)
                .build()
                ;
    }

    public static EventFullDto eventToFullDto(Event event, Long confirmedRequests) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getDescription(),
                CategoryMapper.categoryToDto(event.getCategory()),
                event.getEventDate(),
                LocationMapper.locationToDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                confirmedRequests,
                event.getRequestModeration(),
                UserMapper.userToShortDto(event.getInitiator()),
                event.getState(),
                event.getCreatedOn(),
                event.getPublishedOn(),
                null
        );
    }

    public static EventFullDtoWithComments eventToFullDtoWithComments(Event event, Long confirmedRequests) {
        return EventFullDtoWithComments.builder()
                .withId(event.getId())
                .withTitle(event.getTitle())
                .withAnnotation(event.getAnnotation())
                .withDescription(event.getDescription())
                .withCategory(CategoryMapper.categoryToDto(event.getCategory()))
                .withEventDate(event.getEventDate())
                .withLocation(LocationMapper.locationToDto(event.getLocation()))
                .withPaid(event.getPaid())
                .withParticipantLimit(event.getParticipantLimit())
                .withConfirmedRequests(confirmedRequests)
                .withRequestModeration(event.getRequestModeration())
                .withInitiator(UserMapper.userToShortDto(event.getInitiator()))
                .withState(event.getState())
                .withCreatedOn(event.getCreatedOn())
                .withPublishedOn(event.getPublishedOn())
                .build();
    }

    public static EventShortDto eventToShortDto(Event event, Long confirmedRequests) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                CategoryMapper.categoryToDto(event.getCategory()),
                event.getEventDate(),
                event.getPaid(),
                confirmedRequests,
                UserMapper.userToShortDto(event.getInitiator()),
                null
        );
    }

}
