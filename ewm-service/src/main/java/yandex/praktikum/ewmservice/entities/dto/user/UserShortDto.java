package yandex.praktikum.ewmservice.entities.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {

    private Long id;
    private String name;
}