package yandex.praktikum.ewmservice.repositories;

import org.springframework.data.repository.CrudRepository;
import yandex.praktikum.ewmservice.entities.User;

import java.util.List;

public interface UsersRepository extends CrudRepository<User, Long> {

    List<User> findUsersByIdIn(List<Long> ids);

}
