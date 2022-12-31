package yandex.praktikum.ewmservice.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yandex.praktikum.ewmservice.entities.dto.user.NewUserRequestDto;
import yandex.praktikum.ewmservice.entities.dto.user.UserDto;
import yandex.praktikum.ewmservice.services.admin.AdminUsersService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class AdminUsersController {
    private final AdminUsersService adminUsersService;

    @PostMapping
    public UserDto create(@RequestBody @Valid NewUserRequestDto newUserRequestDto) {
        return adminUsersService.create(newUserRequestDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        adminUsersService.delete(userId);
    }

    @GetMapping
    public List<UserDto> getAll(
            @RequestParam(name = "ids", required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return adminUsersService.getAll(ids, from, size);
    }

}
