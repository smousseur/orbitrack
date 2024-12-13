package com.smousseur.orbitrack.api.repository;

import com.smousseur.orbitrack.model.OrbitalBodySearchResponse;
import com.smousseur.orbitrack.model.entity.OrbitalBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrbitalBodyRepository {
  Mono<OrbitalBody> findById(Integer id);

  Flux<OrbitalBody> findByNameContains(String name);

  Flux<OrbitalBodySearchResponse> search(String likeName, String type, int limit, int offset);

  Mono<Long> count(String likeName, String type);
}
