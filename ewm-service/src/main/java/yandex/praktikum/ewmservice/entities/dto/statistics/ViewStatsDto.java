package yandex.praktikum.ewmservice.entities.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {

    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @NotNull
    private Long hits;

}
