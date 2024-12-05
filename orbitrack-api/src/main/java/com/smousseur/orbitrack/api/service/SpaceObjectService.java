package com.smousseur.orbitrack.api.service;

import com.smousseur.orbitrack.api.model.dto.*;
import com.smousseur.orbitrack.api.model.entity.SpaceObject;
import com.smousseur.orbitrack.api.repository.SpaceObjectRepository;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.GeodeticPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SpaceObjectService {
  private static final Logger logger = LoggerFactory.getLogger(SpaceObjectService.class);
  private final SpaceObjectRepository repository;
  private final SpaceTrackService trackService;
  private final OrekitService orekitService;
  private final SpaceObjectSpeedService speedService;

  @Value("${space.track.init}")
  private boolean doInit;

  @PostConstruct
  @Transactional
  public void initialize() {
    if (doInit) {
      logger.info("Updating datas...");
      trackService
          .getSpaceObjects()
          .map(
              obj ->
                  repository
                      .findByNoradId(obj.getNoradId())
                      .map(spaceObj -> spaceObj.updateFromSpaceTrack(obj))
                      .switchIfEmpty(Mono.just(SpaceObject.fromSpaceTrack(obj))))
          .flatMap(Mono::flux)
          .subscribe(
              obj -> repository.save(obj).subscribe(),
              error -> logger.error("Error !"),
              () -> logger.info("Updating done."));
    }
  }

  public Mono<SpaceObjectDto> findById(Integer id) {
    return repository.findById(id).map(SpaceObjectDto::fromSpaceObjet);
  }

  public Flux<SpaceObjectSearchResponse> findByName(String name) {
    return repository.findByNameContains(name).map(SpaceObjectSearchResponse::new);
  }

  public GeoPosition getObjectPosition(SpaceObjectDto object) {
    // LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    Instant now = Instant.now();
    SpaceObjectTelemetry telemetry = object.telemetry();
    GeodeticPoint geodeticPoint =
        orekitService.computePosition(telemetry.tle1(), telemetry.tle2(), now);
    GeoPosition geoPosition =
        GeoPosition.builder()
            .latitude(FastMath.toDegrees(geodeticPoint.getLatitude()))
            .longitude(FastMath.toDegrees(geodeticPoint.getLongitude()))
            .altitude(geodeticPoint.getAltitude())
            .build();
    Double speed = speedService.computeSpeed(object.id(), geoPosition, now).orElse(0.0);
    geoPosition.setSpeed(speed);
    speedService.updatePosition(object.id(), new SpaceObjectPosition(geoPosition, now));
    return geoPosition;
  }
}
