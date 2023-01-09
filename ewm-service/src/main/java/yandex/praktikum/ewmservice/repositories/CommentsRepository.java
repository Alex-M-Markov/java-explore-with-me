package yandex.praktikum.ewmservice.repositories;

import org.springframework.data.repository.CrudRepository;
import yandex.praktikum.ewmservice.entities.Comment;

public interface CommentsRepository extends CrudRepository<Comment, Long> {

}
