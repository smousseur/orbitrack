package com.smousseur.orbitrack.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrbitalBodySearchResponse {
  String name;
  String objectType;
  String size;
  String countryCode;
  LocalDate launchDate;
  String function;

  public static Mono<OrbitalBodySearchResponse> fromRow(Map<String, Object> sqlRow) {
    return Mono.just(sqlRow)
        .map(
            row ->
                OrbitalBodySearchResponse.builder()
                    .name((String) row.get("name"))
                    .objectType((String) row.get("object_type"))
                    .size((String) row.get("size"))
                    .countryCode((String) row.get("country_code"))
                    .launchDate((LocalDate) row.get("launch_date"))
                    .function((String) row.get("kind"))
                    .build());
  }
}
