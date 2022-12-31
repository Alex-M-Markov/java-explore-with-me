package yandex.praktikum.ewmservice.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import yandex.praktikum.ewmservice.entities.Compilation;

import java.util.List;

public interface CompilationsRepository extends CrudRepository<Compilation, Long> {
    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);

}
