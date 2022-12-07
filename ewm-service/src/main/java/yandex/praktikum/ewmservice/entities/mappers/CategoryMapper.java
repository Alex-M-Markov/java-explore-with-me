package yandex.praktikum.ewmservice.entities.mappers;


import lombok.experimental.UtilityClass;
import yandex.praktikum.ewmservice.entities.Category;
import yandex.praktikum.ewmservice.entities.dto.category.CategoryDto;

@UtilityClass
public class CategoryMapper {

    public static CategoryDto categoryToDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

}
