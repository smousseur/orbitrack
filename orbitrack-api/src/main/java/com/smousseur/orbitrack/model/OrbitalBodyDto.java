package com.smousseur.orbitrack.model;

import com.smousseur.orbitrack.model.entity.OrbitalBody;
import lombok.Builder;

@Builder
public record OrbitalBodyDto(
    Integer id,
    String name,
    Integer noradId,
    String objectId,
    OrbitalBodyType type,
    OrbitalBodySize size,
    OrbitalBodyTelemetry telemetry) {
  public static OrbitalBodyDto fromEntity(OrbitalBody object) {
    return OrbitalBodyDto.builder()
        .id(object.getId())
        .name(object.getName())
        .noradId(object.getNoradId())
        .objectId(object.getObjectId())
        .type(OrbitalBodyType.fromValue(object.getObjectType()))
        .size(OrbitalBodySize.fromValue(object.getSize()))
        .telemetry(OrbitalBodyTelemetry.fromSpaceObject(object))
        .build();
  }
}
