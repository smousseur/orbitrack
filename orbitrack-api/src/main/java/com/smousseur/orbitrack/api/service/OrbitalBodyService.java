package com.smousseur.orbitrack.api.service;

import com.smousseur.orbitrack.api.model.GeoPosition;
import com.smousseur.orbitrack.api.model.OrbitalBodyDto;
import com.smousseur.orbitrack.api.model.OrbitalBodyPosition;
import com.smousseur.orbitrack.api.model.OrbitalBodySearchFastResponse;
import com.smousseur.orbitrack.api.model.OrbitalBodyTelemetry;
import com.smousseur.orbitrack.api.repository.OrbitalBodyRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.GeodeticPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrbitalBodyService {
  private static final Logger logger = LoggerFactory.getLogger(OrbitalBodyService.class);
  private final OrbitalBodyRepository repository;
  private final OrekitService orekitService;
  private final OrbitalBodySpeedService speedService;

  public Mono<OrbitalBodyDto> findById(Integer id) {
    return repository.findById(id).map(OrbitalBodyDto::fromEntity);
  }

  public Flux<OrbitalBodySearchFastResponse> findByName(String name) {
    return repository.findByNameContains(name).map(OrbitalBodySearchFastResponse::new);
  }

  public GeoPosition getObjectPosition(
      OrbitalBodyDto object,
      LocalDateTime startFluxRealtime,
      LocalDateTime startFluxTime,
      Double speedClock) {
    double deltaTime =
        Duration.between(startFluxRealtime, LocalDateTime.now(ZoneOffset.UTC)).toNanos()
            * speedClock;
    Instant currentTime = startFluxTime.plusNanos(Math.round(deltaTime)).toInstant(ZoneOffset.UTC);
    OrbitalBodyTelemetry telemetry = object.telemetry();
    GeodeticPoint geodeticPoint =
        orekitService.computePosition(telemetry.tle1(), telemetry.tle2(), currentTime);
    GeoPosition geoPosition =
        GeoPosition.builder()
            .latitude(FastMath.toDegrees(geodeticPoint.getLatitude()))
            .longitude(FastMath.toDegrees(geodeticPoint.getLongitude()))
            .altitude(geodeticPoint.getAltitude())
            .build();
    Double speed = speedService.computeSpeed(object.id(), geoPosition, currentTime).orElse(0.0);
    geoPosition.setSpeed(speed);
    speedService.updatePosition(object.id(), new OrbitalBodyPosition(geoPosition, currentTime));
    return geoPosition;
  }
}
