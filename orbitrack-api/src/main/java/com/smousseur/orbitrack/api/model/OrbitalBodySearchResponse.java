package com.smousseur.orbitrack.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrbitalBodySearchResponse(
    Integer id,
    Integer noradId,
    String name,
    String cosparId,
    String objectType,
    String size,
    String countryCode,
    LocalDate launchDate) {}
