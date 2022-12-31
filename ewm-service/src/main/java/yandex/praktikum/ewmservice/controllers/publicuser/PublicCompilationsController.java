package yandex.praktikum.ewmservice.controllers.publicuser;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yandex.praktikum.ewmservice.entities.dto.compilations.CompilationDto;
import yandex.praktikum.ewmservice.services.publicuser.PublicCompilationsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
public class PublicCompilationsController {
    private final PublicCompilationsService publicCompilationsService;

    @GetMapping
    public List<CompilationDto> get(
            @RequestParam(required = false) Boolean pinned,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return publicCompilationsService.get(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable long compId) {
        return publicCompilationsService.getById(compId);
    }

}
