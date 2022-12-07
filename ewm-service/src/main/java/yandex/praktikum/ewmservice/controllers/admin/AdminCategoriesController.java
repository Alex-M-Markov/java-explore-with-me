package yandex.praktikum.ewmservice.controllers.admin;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import yandex.praktikum.ewmservice.entities.dto.category.CategoryDto;
import yandex.praktikum.ewmservice.entities.dto.category.NewCategoryDto;
import yandex.praktikum.ewmservice.services.admin.AdminCategoriesService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {
    private final AdminCategoriesService adminCategoriesService;

    @PostMapping
    public CategoryDto create(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return adminCategoriesService.create(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto update(@RequestBody @Valid CategoryDto categoryDto) {
        return adminCategoriesService.update(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable long catId) {
        adminCategoriesService.delete(catId);
    }

}