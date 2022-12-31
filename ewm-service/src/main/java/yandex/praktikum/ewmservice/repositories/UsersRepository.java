package yandex.praktikum.ewmservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import yandex.praktikum.ewmservice.entities.User;

import java.util.List;

public interface UsersRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    List<User> findUsersByIdIn(List<Long> ids);

    Page<User> findAll(Pageable pageable);

}
