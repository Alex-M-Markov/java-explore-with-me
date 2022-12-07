package yandex.praktikum.ewmservice.services.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.ewmservice.entities.Category;
import yandex.praktikum.ewmservice.entities.dto.category.CategoryDto;
import yandex.praktikum.ewmservice.entities.dto.category.NewCategoryDto;
import yandex.praktikum.ewmservice.entities.mappers.CategoryMapper;
import yandex.praktikum.ewmservice.exceptions.CategoryNotFoundException;
import yandex.praktikum.ewmservice.repositories.CategoriesRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AdminCategoriesService {
    private final CategoriesRepository categoriesRepository;

    @Transactional
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        log.info("Создается новая категория {}", newCategoryDto.getName());
        CategoryDto categoryDtoReturn = CategoryMapper.categoryToDto(
                categoriesRepository.save(Category.builder()
                        .withName(newCategoryDto.getName())
                        .build()));
        log.info("Категория {} успешно создана", categoryDtoReturn.getName());
        return categoryDtoReturn;
    }

    @Transactional
    public CategoryDto update(CategoryDto categoryDto) {
        log.info("Обновляется категория {}", categoryDto.getName());
        Category categoryToUpdate = categoriesRepository.findById(categoryDto.getId()).orElseThrow(() ->
                new CategoryNotFoundException(categoryDto.getId()));
        categoryToUpdate.setName(categoryDto.getName());
        CategoryDto categoryDtoReturn = CategoryMapper.categoryToDto(categoryToUpdate);
        log.info("Вещь {} успешно обновлена", categoryDtoReturn.getName());
        return categoryDto;
    }

    @Transactional
    public void delete(Long catId) {
        log.info("Удаляется категория {}", catId);
        categoriesRepository.deleteById(catId);
        log.info("Категория {} успешно удалена", catId);
    }

}
