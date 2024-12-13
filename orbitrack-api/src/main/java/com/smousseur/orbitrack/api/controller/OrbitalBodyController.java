package com.smousseur.orbitrack.api.controller;

import com.smousseur.orbitrack.api.service.OrbitalBodySearchService;
import com.smousseur.orbitrack.api.service.OrbitalBodyService;
import com.smousseur.orbitrack.model.GeoPosition;
import com.smousseur.orbitrack.model.OrbitalBodyDto;
import com.smousseur.orbitrack.model.OrbitalBodySearchFastResponse;
import com.smousseur.orbitrack.model.OrbitalBodySearchResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class OrbitalBodyController {
  private static final int MAX_SEARCH_ITEMS = 10;
  private final OrbitalBodyService service;
  private final OrbitalBodySearchService searchService;

  @GetMapping("/api/objects/{objectId}")
  public Mono<OrbitalBodyDto> getObject(@PathVariable("objectId") Integer objectId) {
    return service.findById(objectId);
  }

  @GetMapping("/api/objects/search")
  public Mono<Page<OrbitalBodySearchResponse>> search(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "type", required = false) String type,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    return searchService.search(name, type, page, size);
  }

  @GetMapping("/api/objects")
  public Flux<OrbitalBodySearchFastResponse> findByName(
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
