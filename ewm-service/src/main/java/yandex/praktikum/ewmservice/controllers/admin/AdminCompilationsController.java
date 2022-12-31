package yandex.praktikum.ewmservice.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import yandex.praktikum.ewmservice.entities.dto.compilations.CompilationDto;
import yandex.praktikum.ewmservice.entities.dto.compilations.NewCompilationDto;
import yandex.praktikum.ewmservice.services.admin.AdminCompilationsService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationsController {
    private final AdminCompilationsService adminCompilationsService;


    @PostMapping
    public CompilationDto add(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return adminCompilationsService.add(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void delete(@PathVariable long compId) {
        adminCompilationsService.delete(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEvent(
            @PathVariable long compId,
            @PathVariable long eventId) {
        adminCompilationsService.deleteEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEvent(
            @PathVariable long compId,
            @PathVariable long eventId) {
        adminCompilationsService.addEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void deletePin(@PathVariable long compId) {
        adminCompilationsService.deletePin(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void addPin(@PathVariable long compId) {
        adminCompilationsService.addPin(compId);
    }

}