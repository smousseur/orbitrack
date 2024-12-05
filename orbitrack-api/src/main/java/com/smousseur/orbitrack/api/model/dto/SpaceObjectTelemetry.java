package com.smousseur.orbitrack.api.model.dto;

import com.smousseur.orbitrack.api.model.entity.SpaceObject;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record SpaceObjectTelemetry(
    Float period,
    Float eccentricity,
    Float inclination,
    Float apoapsis,
    Float periapsis,
    String tle1,
    String tle2,
    LocalDateTime epoch) {
  public static SpaceObjectTelemetry fromSpaceObject(SpaceObject object) {
    return SpaceObjectTelemetry.builder()
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
