package com.smousseur.orbitrack.api.service;

import com.smousseur.orbitrack.api.model.dto.CookieDto;
import com.smousseur.orbitrack.api.model.dto.SpaceTrackObject;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SpaceTrackService {
  private static final String URL = "https://www.space-track.org";
  private static final String COOKIE_NAME = "chocolatechip";
  private static final String SPACE_DATA_URI =
      "basicspacedata/query/class/gp/EPOCH/%3Enow-30/orderby/NORAD_CAT_ID,LAUNCH_DATE,EPOCH/format/json";

  @Value("${space.track.username}")
  private String username;

  @Value("${space.track.password}")
  private String password;

  private final WebClient webClient = WebClient.builder().baseUrl(URL).build();

  public Flux<SpaceTrackObject> getSpaceObjects() {
    return getObjectsData(login());
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
