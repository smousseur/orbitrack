package com.smousseur.orbitrack.api.controller;

import com.smousseur.orbitrack.api.model.*;
import com.smousseur.orbitrack.api.service.OrbitalBodySearchService;
import com.smousseur.orbitrack.api.service.OrbitalBodyService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class OrbitalBodyController {
  private final OrbitalBodyService service;
  private final OrbitalBodySearchService searchService;

  @GetMapping("/api/objects/{objectId}")
  public Mono<OrbitalBodyDto> getObject(@PathVariable("objectId") Integer objectId) {
    return service.findById(objectId);
  }

  @GetMapping("/api/objects/search")
  public Mono<PageResult<OrbitalBodySearchResponse>> search(
      @RequestParam(value = "noradId", required = false) Integer noradId,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "cosparId", required = false) String cosparId,
      @RequestParam(value = "type", required = false) String type,
      @RequestParam(value = "countryCode", required = false) String countryCode,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    return searchService.search(noradId, name, cosparId, type, countryCode, page, size);
  }

  @GetMapping("/api/objects")
  public Flux<OrbitalBodySearchFastResponse> findByName(
      @RequestParam("name") String name,
      @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
    return service.findByName(name).take(limit);
  }

  @GetMapping(value = "/api/objects/position/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<GeoPosition> getObjectsPositionStream(
      @RequestParam("ids") String objectIds,
      @RequestParam("time") String time,
      @RequestParam("speed") Double speedClock) {
    LocalDateTime startFluxTime = LocalDateTime.parse(time);
    LocalDateTime startFluxRealtime = LocalDateTime.now(ZoneOffset.UTC);
    return Flux.interval(Duration.ofMillis(30))
        .flatMapIterable(tick -> Arrays.stream(objectIds.split(",")).map(Integer::valueOf).toList())
        .flatMap(service::findById)
        .map(obj -> service.getObjectPosition(obj, startFluxRealtime, startFluxTime, speedClock));
  }
}
