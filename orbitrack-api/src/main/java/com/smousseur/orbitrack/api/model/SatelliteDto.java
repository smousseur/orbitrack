package com.smousseur.orbitrack.api.model;

import com.smousseur.orbitrack.model.entity.Satellite;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SatelliteDto {
  private Integer id;
  private String name;
  private String objectId;
  private String kind;
  private String operator;
  private String contractors;
  private String equipment;
  private String configuration;
  private String propulsion;
  private String power;
  private String lifetime;
  private String mass;
  private String orbit;
  private Boolean hasImage;

  public static SatelliteDto fromEntity(Satellite satellite) {
    return SatelliteDto.builder()
        .id(satellite.getId())
        .objectId(satellite.getObjectId())
        .kind(satellite.getKind())
        .operator(satellite.getOperator())
        .contractors(satellite.getContractors())
        .equipment(satellite.getEquipment())
        .configuration(satellite.getConfiguration())
        .propulsion(satellite.getPropulsion())
        .power(satellite.getPower())
        .lifetime(satellite.getLifetime())
        .mass(satellite.getMass())
        .orbit(satellite.getOrbit() != null ? satellite.getOrbit().split(";")[0] : null)
        .hasImage(satellite.getHasImage())
        .build();
  }
}
