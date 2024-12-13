package com.smousseur.orbitrack.scraper.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScrapSatellite {
  private Integer orbitalBodyId;
  private String type;
  private String operator;
  private String contractors;
  private String equipment;
  private String configuration;
  private String propulsion;
  private String power;
  private String lifetime;
  private String mass;
  private String orbit;
  private String image;
  private String cospar;

  public static ScrapSatellite from(
      String type,
      String operator,
      String contractors,
      String equipment,
      String configuration,
      String propulsion,
      String power,
      String lifetime,
      String mass,
      String orbit,
      String cospar,
      String image) {
    return ScrapSatellite.builder()
        .type(type)
        .operator(operator)
        .contractors(contractors)
        .equipment(equipment)
        .configuration(configuration)
        .propulsion(propulsion)
        .power(power)
        .lifetime(lifetime)
        .mass(mass)
        .orbit(orbit)
        .cospar(cospar)
        .image(image)
        .build();
  }
}
