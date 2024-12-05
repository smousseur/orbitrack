package com.smousseur.orbitrack.api.model.dto;

import com.smousseur.orbitrack.api.model.entity.SpaceObject;
import lombok.Builder;

@Builder
public record SpaceObjectDto(
    Integer id,
    String name,
    Integer noradId,
    String objectId,
    SpaceObjectType type,
    SpaceObjectSize size,
    SpaceObjectTelemetry telemetry) {
  public static SpaceObjectDto fromSpaceObjet(SpaceObject object) {
    return SpaceObjectDto.builder()
        .id(object.getId())
        .name(object.getName())
        .noradId(object.getNoradId())
        .objectId(object.getObjectId())
        .type(SpaceObjectType.fromValue(object.getObjectType()))
        .size(SpaceObjectSize.fromValue(object.getSize()))
        .telemetry(SpaceObjectTelemetry.fromSpaceObject(object))
        .build();
  }
}
