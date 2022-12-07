package yandex.praktikum.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.entities.EndpointHit;
import yandex.praktikum.entities.dto.EndpointHitDto;
import yandex.praktikum.entities.mappers.EndPointHitMapper;
import yandex.praktikum.repositories.HitsRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class HitsService {
    private final HitsRepository hitsRepository;

    @Transactional
    public EndpointHitDto add(EndpointHit endpointHit) {
        return EndPointHitMapper.endpointHitToDto(hitsRepository.save(endpointHit));
    }
}
