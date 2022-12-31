package yandex.praktikum.ewmservice.entities.mappers;

import lombok.experimental.UtilityClass;
import yandex.praktikum.ewmservice.entities.Location;
import yandex.praktikum.ewmservice.entities.dto.location.LocationDto;

@UtilityClass
public class LocationMapper {
    public static LocationDto locationToDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }
}
