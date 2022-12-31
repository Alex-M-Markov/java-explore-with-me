package yandex.praktikum.ewmservice.controllers.publicuser;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yandex.praktikum.ewmservice.entities.dto.category.CategoryDto;
import yandex.praktikum.ewmservice.services.publicuser.PublicCategoriesService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class PublicCategoriesController {

    private final PublicCategoriesService publicCategoriesService;

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable long catId) {
        return publicCategoriesService.getById(catId);
    }

    @GetMapping
    public List<CategoryDto> getAll(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return publicCategoriesService.getAll(from, size);
    }

}
