package com.smousseur.orbitrack.model.entity;

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
@Table("orbital_bodies")
public class OrbitalBody {
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
}
