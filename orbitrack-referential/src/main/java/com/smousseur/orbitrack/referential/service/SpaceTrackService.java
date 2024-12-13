package com.smousseur.orbitrack.referential.service;

import com.smousseur.orbitrack.model.entity.OrbitalBody;
import com.smousseur.orbitrack.referential.model.CookieDto;
import com.smousseur.orbitrack.referential.model.SpaceTrackObject;
import com.smousseur.orbitrack.referential.repository.OrbitalBodyRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class SpaceTrackService {
  private static final Logger logger = LoggerFactory.getLogger(SpaceTrackService.class);
  private static final String COOKIE_NAME = "chocolatechip";
  private static final String SPACE_DATA_URI =
      "basicspacedata/query/class/gp/EPOCH/%3Enow-30/orderby/NORAD_CAT_ID,LAUNCH_DATE,EPOCH/format/json";

  @Value("${space.track.username}")
  private String username;

  @Value("${space.track.password}")
  private String password;

  private final OrbitalBodyRepository repository;
  private final WebClient webClient;

  public Flux<SpaceTrackObject> getSpaceObjects() {
    return getObjectsData(login());
  }

  public void process() {
    List<OrbitalBody> bodies =
        getSpaceObjects()
            .flatMap(
                obj ->
                    repository
                        .findByNoradId(obj.getNoradId())
                        .map(obj::updateFromSpaceTrack)
                        .switchIfEmpty(Mono.just(SpaceTrackObject.fromSpaceTrack(obj))))
            .collectList()
            .block();
    if (bodies != null) {
      repository.saveAll(bodies).subscribe();
    }
  }

  private Mono<CookieDto> login() {
    return webClient
        .post()
        .uri("ajaxauth/login")
        .body(
            BodyInserters.fromFormData(
                MultiValueMap.fromSingleValue(Map.of("identity", username, "password", password))))
        .exchangeToMono(
            response -> Mono.just(Objects.requireNonNull(response.cookies().getFirst(COOKIE_NAME))))
        .map(cookie -> CookieDto.builder().name(cookie.getName()).value(cookie.getValue()).build());
  }

  private Flux<SpaceTrackObject> getObjectsData(Mono<CookieDto> cookieDto) {
    return cookieDto.flatMapMany(
        cookie ->
            webClient
                .get()
                .uri(SPACE_DATA_URI)
                .cookie(cookie.getName(), cookie.getValue())
                .retrieve()
                .bodyToFlux(SpaceTrackObject.class));
  }
}
