package yandex.praktikum.ewmservice.entities.mappers;

import lombok.experimental.UtilityClass;
import yandex.praktikum.ewmservice.entities.User;
import yandex.praktikum.ewmservice.entities.dto.user.NewUserRequestDto;
import yandex.praktikum.ewmservice.entities.dto.user.UserDto;
import yandex.praktikum.ewmservice.entities.dto.user.UserShortDto;

@UtilityClass
public class UserMapper {

    public static User fromNewUserRequestDto(NewUserRequestDto newUserRequestDto) {
        return User.builder()
                .withName(newUserRequestDto.getName())
                .withEmail(newUserRequestDto.getEmail())
                .build();
    }

    public static UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static UserShortDto userToShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
