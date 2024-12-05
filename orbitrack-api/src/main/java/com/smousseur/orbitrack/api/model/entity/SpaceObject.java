package com.smousseur.orbitrack.api.model.entity;

import com.smousseur.orbitrack.api.model.dto.SpaceTrackObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("space_objects")
public class SpaceObject {
  @Id private Integer id;

  @Column("norad_id")
  private Integer noradId;

  private String name;

  @Column("object_id")
  private String objectId;

  @Column("object_type")
  private String objectType;

  private String size;

  @Column("country_code")
  private String countryCode;

  @Column("launch_date")
  private LocalDate launchDate;

  @Column("launch_site")
  private String launchSite;

  private Float period;

  private Float eccentricity;

  private Float inclination;

  private Float apoapsis;

  private Float periapsis;

  private String tle1;

  private String tle2;

  private LocalDateTime epoch;

  public SpaceObject updateFromSpaceTrack(SpaceTrackObject trackObject) {
    this.period = trackObject.getPeriod();
    this.eccentricity = trackObject.getEccentricity();
    this.inclination = trackObject.getInclination();
    this.apoapsis = trackObject.getApoapsis();
    this.periapsis = trackObject.getPeriapsis();
    this.tle1 = trackObject.getTle1();
    this.tle2 = trackObject.getTle2();
    this.epoch = trackObject.getEpoch();
    return this;
  }

  public static SpaceObject fromSpaceTrack(SpaceTrackObject trackObject) {
    return SpaceObject.builder()
        .noradId(trackObject.getNoradId())
        .name(trackObject.getName())
        .objectId(trackObject.getObjectId())
        .objectType(trackObject.getObjectType())
        .size(trackObject.getSize())
        .countryCode(trackObject.getCountryCode())
        .launchDate(trackObject.getLaunchDate())
        .launchSite(trackObject.getLaunchSite())
        .period(trackObject.getPeriod())
        .eccentricity(trackObject.getEccentricity())
        .inclination(trackObject.getInclination())
        .apoapsis(trackObject.getApoapsis())
        .periapsis(trackObject.getPeriapsis())
        .tle1(trackObject.getTle1())
        .tle2(trackObject.getTle2())
        .epoch(trackObject.getEpoch())
        .build();
  }
}
