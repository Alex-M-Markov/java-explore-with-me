package yandex.praktikum.entities.mappers;

import lombok.experimental.UtilityClass;
import yandex.praktikum.entities.EndpointHit;
import yandex.praktikum.entities.dto.EndpointHitDto;

@UtilityClass
public class EndPointHitMapper {
    public static EndpointHitDto endpointHitToDto(EndpointHit endpointHit) {
        return new EndpointHitDto(endpointHit.getId(), endpointHit.getApp(), endpointHit.getUri(), endpointHit.getIp(),
                endpointHit.getTimestamp());
    }
}
