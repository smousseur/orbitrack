package com.smousseur.orbitrack.api.controller;

import com.smousseur.orbitrack.api.model.SatelliteDto;
import com.smousseur.orbitrack.api.service.SatelliteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class SatelliteController {
  private final SatelliteService service;

  @GetMapping("/api/satellites")
  public Mono<SatelliteDto> getSatellite(
      @RequestParam("objectId") Integer objectId,
      @RequestParam("satelliteName") String satelliteName) {
    return service.getSatelliteDetails(objectId, satelliteName);
  }
}
