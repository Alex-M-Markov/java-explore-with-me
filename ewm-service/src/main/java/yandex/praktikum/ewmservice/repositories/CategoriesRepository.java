package yandex.praktikum.ewmservice.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import yandex.praktikum.ewmservice.entities.Category;

import java.util.List;

public interface CategoriesRepository extends CrudRepository<Category, Long> {
    List<Category> findAll(Pageable pageable);

    Category findCategoryByName(String name);
}
