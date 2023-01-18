package yandex.praktikum.ewmservice.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import yandex.praktikum.ewmservice.entities.Comment;
import yandex.praktikum.ewmservice.entities.Event;

import java.util.List;

public interface CommentsRepository extends CrudRepository<Comment, Long> {

    List<Comment> findAllByEvent(Event event);

    void deleteAllByUserId(long userId);

    List<Comment> getCommentsByUserId(long userId, Pageable pageable);

    List<Comment> getCommentsByTextContainingIgnoreCase(String text, Pageable pageable);

}
