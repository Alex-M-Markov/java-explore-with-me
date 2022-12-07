package yandex.praktikum.ewmservice.client;

import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import yandex.praktikum.ewmservice.entities.dto.statistics.EndpointHitDto;
import yandex.praktikum.ewmservice.entities.dto.statistics.StatsRequestDto;
import yandex.praktikum.ewmservice.entities.dto.statistics.ViewStatsDto;

import java.util.List;

@Service
public class StatsWebClient {
    @Value("${STATSSERVER-URL}")
    private String serverUrl;
    private final WebClient webClient;

    public StatsWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public EndpointHitDto addHit(EndpointHitDto endpointHitDto) {
        Mono<EndpointHitDto> endpointHitDtoMono = webClient.post()
                .uri(serverUrl + "/hit")
                .body(Mono.just(endpointHitDto), EndpointHitDto.class)
                .retrieve()
                .bodyToMono(EndpointHitDto.class);
        return endpointHitDtoMono.share().block();
    }

    @SneakyThrows
    public List<ViewStatsDto> getViews(StatsRequestDto statsRequestDto) {
        URIBuilder b = new URIBuilder(serverUrl);
        b.addParameter("start", statsRequestDto.getStart().toString());
        b.addParameter("end", statsRequestDto.getEnd().toString());
        b.addParameter("uris", statsRequestDto.getUris().toString());
        b.addParameter("unique", statsRequestDto.getUnique().toString());

        return webClient.get()
                .uri(b.build())
                .retrieve()
                .bodyToFlux(ViewStatsDto.class)
                .collectList()
                .block();
    }
}