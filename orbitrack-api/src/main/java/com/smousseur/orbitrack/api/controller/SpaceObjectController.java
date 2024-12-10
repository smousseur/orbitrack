package com.smousseur.orbitrack.api.controller;

import com.smousseur.orbitrack.api.model.dto.GeoPosition;
import com.smousseur.orbitrack.api.model.dto.SpaceObjectDto;
import com.smousseur.orbitrack.api.model.dto.SpaceObjectSearchResponse;
import com.smousseur.orbitrack.api.service.SpaceObjectService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SpaceObjectController {
  private static final int MAX_SEARCH_ITEMS = 10;
  private final SpaceObjectService service;

  @GetMapping("/api/objects/{objectId}")
  public Mono<SpaceObjectDto> getObject(@PathVariable("objectId") Integer objectId) {
    return service.findById(objectId);
  }

  @GetMapping("/api/objects")
  public Flux<SpaceObjectSearchResponse> findByName(
      @RequestParam("name") String name, @RequestParam("limit") Optional<Integer> limit) {
    int fluxLimit = limit.orElse(MAX_SEARCH_ITEMS);
    return service.findByName(name).take(fluxLimit);
  }

  @GetMapping(
      value = "/api/objects/{objectId}/position/stream",
      produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<GeoPosition> getObjectPositionStream(
      @PathVariable("objectId") Integer objectId,
      @RequestParam("time") String time,
      @RequestParam("speed") Double speedClock) {
    LocalDateTime startFluxTime = LocalDateTime.parse(time);
    LocalDateTime startFluxRealtime = LocalDateTime.now(ZoneOffset.UTC);
    return Flux.interval(Duration.ofMillis(33))
        .map(tick -> service.findById(objectId))
        .flatMap(Mono::flux)
        .map(obj -> service.getObjectPosition(obj, startFluxRealtime, startFluxTime, speedClock));
  }
}
