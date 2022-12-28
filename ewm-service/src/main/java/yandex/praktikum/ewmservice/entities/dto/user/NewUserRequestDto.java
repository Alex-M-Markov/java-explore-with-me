package yandex.praktikum.ewmservice.entities.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequestDto {

    @NotBlank(message = "Задайте имя пользователя")
    private String name;
    @Email(message = "Укажите свой e-mail")
    @Size(max = 512)
    @NotBlank
    private String email;
}