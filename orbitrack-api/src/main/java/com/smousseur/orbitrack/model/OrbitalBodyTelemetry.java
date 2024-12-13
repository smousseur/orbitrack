package com.smousseur.orbitrack.model;

import com.smousseur.orbitrack.model.entity.OrbitalBody;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record OrbitalBodyTelemetry(
    Float period,
    Float eccentricity,
    Float inclination,
    Float apoapsis,
    Float periapsis,
    String tle1,
    String tle2,
    LocalDateTime epoch) {
  public static OrbitalBodyTelemetry fromSpaceObject(OrbitalBody object) {
    return OrbitalBodyTelemetry.builder()
        .period(object.getPeriod())
        .eccentricity(object.getEccentricity())
        .inclination(object.getInclination())
        .apoapsis(object.getApoapsis())
        .periapsis(object.getPeriapsis())
        .tle1(object.getTle1())
        .tle2(object.getTle2())
        .epoch(object.getEpoch())
        .build();
  }
}
