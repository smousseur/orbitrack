package com.smousseur.orbitrack.referential.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smousseur.orbitrack.model.entity.OrbitalBody;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpaceTrackObject {
  @JsonProperty("NORAD_CAT_ID")
  private Integer noradId;

  @JsonProperty("OBJECT_NAME")
  private String name;

  @JsonProperty("OBJECT_ID")
  private String objectId;

  @JsonProperty("OBJECT_TYPE")
  private String objectType;

  @JsonProperty("COUNTRY_CODE")
  private String countryCode;

  @JsonProperty("RCS_SIZE")
  private String size;

  @JsonProperty("TLE_LINE1")
  private String tle1;

  @JsonProperty("TLE_LINE2")
  private String tle2;

  @JsonProperty("LAUNCH_DATE")
  private LocalDate launchDate;

  @JsonProperty("SITE")
  private String launchSite;

  @JsonProperty("PERIOD")
  private Float period;

  @JsonProperty("INCLINATION")
  private Float inclination;

  @JsonProperty("ECCENTRICITY")
  private Float eccentricity;

  @JsonProperty("APOAPSIS")
  private Float apoapsis;

  @JsonProperty("PERIAPSIS")
  private Float periapsis;

  @JsonProperty("EPOCH")
  private LocalDateTime epoch;

  public OrbitalBody updateFromSpaceTrack(OrbitalBody orbitalBody) {
    orbitalBody.setPeriod(this.getPeriod());
    orbitalBody.setEccentricity(this.getEccentricity());
    orbitalBody.setInclination(this.getInclination());
    orbitalBody.setApoapsis(this.getApoapsis());
    orbitalBody.setPeriapsis(this.getPeriapsis());
    orbitalBody.setTle1(this.getTle1());
    orbitalBody.setTle2(this.getTle2());
    orbitalBody.setEpoch(this.getEpoch());
    return orbitalBody;
  }

  public static OrbitalBody fromSpaceTrack(SpaceTrackObject trackObject) {
    return OrbitalBody.builder()
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
