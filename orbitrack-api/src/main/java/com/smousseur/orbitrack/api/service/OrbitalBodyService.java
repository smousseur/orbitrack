package com.smousseur.orbitrack.api.service;

import com.smousseur.orbitrack.api.model.GeoPosition;
import com.smousseur.orbitrack.api.model.OrbitalBodyDto;
import com.smousseur.orbitrack.api.model.OrbitalBodySearchFastResponse;
import com.smousseur.orbitrack.api.model.OrbitalBodyTelemetry;
import com.smousseur.orbitrack.api.repository.OrbitalBodyRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.graalvm.collections.Pair;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.GeodeticPoint;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrbitalBodyService {
  private final OrbitalBodyRepository repository;
  private final OrekitService orekitService;

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
    Pair<GeodeticPoint, Double> pointAndSpeed =
        orekitService.computePosition(telemetry.tle1(), telemetry.tle2(), currentTime);

    GeodeticPoint geodeticPoint = pointAndSpeed.getLeft();
    return GeoPosition.builder()
        .objectId(object.id())
        .objectName(object.name())
        .latitude(FastMath.toDegrees(geodeticPoint.getLatitude()))
        .longitude(FastMath.toDegrees(geodeticPoint.getLongitude()))
        .altitude(geodeticPoint.getAltitude())
        .speed(pointAndSpeed.getRight())
        .build();
    //    Double speed = speedService.computeSpeed(object.id(), geoPosition,
    // currentTime).orElse(0.0);
    //    geoPosition.setSpeed(speed);
    //    speedService.updatePosition(object.id(), new OrbitalBodyPosition(geoPosition,
    // currentTime));
    //    return geoPosition;
  }
}
