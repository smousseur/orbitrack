package com.smousseur.orbitrack.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
}
