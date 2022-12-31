package yandex.praktikum.ewmservice.entities.dto.category;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {

    @NotBlank(message = "Укажите название категории")
    @Size(max = 255)
    private String name;
}
