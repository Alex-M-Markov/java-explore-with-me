package yandex.praktikum.ewmservice.services.publicuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.ewmservice.entities.dto.category.CategoryDto;
import yandex.praktikum.ewmservice.entities.mappers.CategoryMapper;
import yandex.praktikum.ewmservice.exceptions.CategoryNotFoundException;
import yandex.praktikum.ewmservice.repositories.CategoriesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PublicCategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoryDto getById(Long categoryId) {
        log.info("Получаем категорию #{}", categoryId);
        return CategoryMapper.categoryToDto(
                categoriesRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId)));
    }

    public List<CategoryDto> getAll(Integer from, Integer size) {
        log.info("Получаем все категории, начиная с #{}", from);
        return categoriesRepository.findAll(PageRequest.of(from, size))
                .stream()
                .map(CategoryMapper::categoryToDto)
                .collect(Collectors.toList());
    }
}
