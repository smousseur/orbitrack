package com.smousseur.orbitrack.api.repository;

import com.smousseur.orbitrack.model.OrbitalBodySearchResponse;
import com.smousseur.orbitrack.model.entity.OrbitalBody;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class OrbitalBodyRepositoryImpl implements OrbitalBodyRepository {
  private final DatabaseClient databaseClient;

  @Override
  public Mono<OrbitalBody> findById(Integer id) {
    // String requestById = OrbitalBodyRequests.SELECT_CLAUSE
    return null;
  }

  @Override
  public Flux<OrbitalBody> findByNameContains(String name) {
    return null;
  }

  @Override
  public Flux<OrbitalBodySearchResponse> search(
      String likeName, String type, int limit, int offset) {
    String request =
        OrbitalBodyRequests.SELECT_CLAUSE
            + OrbitalBodyRequests.FROM_CLAUSE
            + OrbitalBodyRequests.WHERE_CLAUSE
            + " LIMIT :limit OFFSET :offset";

    DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(request).bind("likeName", likeName);
    spec = type != null ? spec.bind("type", type) : spec.bindNull("type", String.class);
    spec = spec.bind("limit", limit).bind("offset", offset);
    return spec.fetch().all().flatMap(OrbitalBodySearchResponse::fromRow);
  }

  @Override
  public Mono<Long> count(String likeName, String type) {
    String request =
        OrbitalBodyRequests.COUNT_CLAUSE
            + OrbitalBodyRequests.FROM_CLAUSE
            + OrbitalBodyRequests.WHERE_CLAUSE;
    DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(request).bind("likeName", likeName);
    spec = type != null ? spec.bind("type", type) : spec.bindNull("type", String.class);
    return spec.fetch().one().map(resultCount -> (long) resultCount.get("row_count"));
  }
}
