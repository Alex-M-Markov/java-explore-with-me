package yandex.praktikum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yandex.praktikum.entities.EndpointHit;
import yandex.praktikum.entities.dto.EndpointHitDto;
import yandex.praktikum.services.HitsService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class HitsController {
    private final HitsService hitsService;

    @PostMapping("/hit")
    public EndpointHitDto add(@RequestBody @Valid EndpointHit endpointHit) {
        return hitsService.add(endpointHit);
    }

}
