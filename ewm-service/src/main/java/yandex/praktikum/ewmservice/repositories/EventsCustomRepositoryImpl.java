package yandex.praktikum.ewmservice.repositories;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.SortOptions;
import yandex.praktikum.ewmservice.entities.State;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventsCustomRepositoryImpl implements EventsCustomRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Event> getEventsPublic(String annotation, List<Long> categories, Boolean paid,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                       SortOptions sort, Integer from, Integer size) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (annotation != null && !annotation.isEmpty()) {
            String searchText = annotation.toLowerCase();
            predicates.add(cb.equal(eventRoot.get("state"), State.PUBLISHED));
            predicates.add(cb.or(
                    cb.like(cb.lower(eventRoot.get("annotation")), "%" + searchText + "%"),
                    cb.like(cb.lower(eventRoot.get("description")), "%" + searchText + "%")));
            addCategoriesPredicate(categories, cb, eventRoot, predicates);
        }
        if (paid != null) {
            predicates.add(cb.equal(eventRoot.get("paid"), paid));
        }
        if (sort != null) {
            if (sort.toString().equals("EVENT_DATE")) {
                query.orderBy(cb.desc(eventRoot.get("eventDate")));
            } else if (sort.toString().equals("VIEWS")) {
                query.orderBy(cb.desc(eventRoot.get("views")));
            }
        }
        predicatesSetting(rangeStart, rangeEnd, cb, eventRoot, predicates);
        if (onlyAvailable != null && onlyAvailable) {
            predicates.add(cb.or(
                    cb.le(eventRoot.get("confirmedRequests"), eventRoot.get("participantLimit")),
                    cb.le(eventRoot.get("participantLimit"), 0)));
        }

        query.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(query).setMaxResults(size).setFirstResult(from).getResultList();
    }

    private void predicatesSetting(LocalDateTime rangeStart, LocalDateTime rangeEnd, CriteriaBuilder cb, Root<Event> eventRoot, List<Predicate> predicates) {
        if (rangeStart == null && rangeEnd == null) {
            predicates.add(cb.greaterThan(eventRoot.get("eventDate"), LocalDateTime.now()));
        }
        if (rangeStart != null) {
            predicates.add(cb.greaterThan(eventRoot.get("eventDate"), rangeStart));
        }
        if (rangeEnd != null) {
            predicates.add(cb.lessThan(eventRoot.get("eventDate"), rangeEnd));
        }
    }

    private void addCategoriesPredicate(List<Long> categories, CriteriaBuilder cb, Root<Event> eventRoot, List<Predicate> predicates) {
        if (categories != null && !categories.isEmpty()) {
            CriteriaBuilder.In<Long> listOfCats = cb.in(eventRoot.get("category"));
            for (Long catId : categories) {
                listOfCats.value(catId);
            }
            predicates.add(listOfCats);
        }
    }

    @Override
    public List<Event> getEventsAdmin(List<Long> users, List<State> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                      Integer from, Integer size) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (users != null && !users.isEmpty()) {
            CriteriaBuilder.In<Long> listOfUsers = cb.in(eventRoot.get("initiator"));
            for (Long userId : users) {
                listOfUsers.value(userId);
            }
            predicates.add(listOfUsers);
        }

        if (states != null && !states.isEmpty()) {
            CriteriaBuilder.In<State> listOfStates = cb.in(eventRoot.get("state"));
            for (State state : states) {
                listOfStates.value(state);
            }
            predicates.add(listOfStates);
        }

        addCategoriesPredicate(categories, cb, eventRoot, predicates);
        predicatesSetting(rangeStart, rangeEnd, cb, eventRoot, predicates);

        query.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(query).setMaxResults(size).setFirstResult(from).getResultList();
    }

}