package yandex.praktikum.ewmservice.entities.dto.compilations;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    @NotNull
    private List<Long> events;
    private boolean pinned = false;
    @NotBlank(message = "Укажите название подборки")
    @Size(max = 255)
    private String title;

}
