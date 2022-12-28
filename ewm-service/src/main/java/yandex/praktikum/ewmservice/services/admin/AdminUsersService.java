package yandex.praktikum.ewmservice.services.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.ewmservice.entities.dto.user.NewUserRequestDto;
import yandex.praktikum.ewmservice.entities.dto.user.UserDto;
import yandex.praktikum.ewmservice.entities.mappers.UserMapper;
import yandex.praktikum.ewmservice.repositories.UsersRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AdminUsersService {

    private final UsersRepository usersRepository;

    @Transactional
    public UserDto create(NewUserRequestDto newUserRequestDto) {
        log.info("Создается новый пользователь {}", newUserRequestDto.getName());
        UserDto userDtoReturn = UserMapper.userToDto(usersRepository.save(
                UserMapper.fromNewUserRequestDto(newUserRequestDto)));
        log.info("Пользователь {} успешно создан", userDtoReturn.getName());
        return userDtoReturn;
    }

    @Transactional
    public void delete(Long userId) {
        log.info("Удаляется пользователь {}", userId);
        usersRepository.deleteById(userId);
        log.info("Пользователь {} успешно удален", userId);
    }

    public List<UserDto> getAll(List<Long> ids, int from, int size) {
        if (ids.isEmpty()) {
            log.info("Получаем всех пользователей, начиная с #{}", from);
            return usersRepository.findAll(PageRequest.of(from, size))
                    .stream()
                    .map(UserMapper::userToDto)
                    .collect(Collectors.toList());
        } else {
            log.info("Получаем пользователей #{}", ids);
            return usersRepository.findUsersByIdIn(ids)
                    .stream()
                    .map(UserMapper::userToDto)
                    .collect(Collectors.toList());
        }
    }

}
