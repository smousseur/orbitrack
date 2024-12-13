package com.smousseur.orbitrack.referential.repository;

import com.smousseur.orbitrack.model.entity.OrbitalBody;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface OrbitalBodyRepository extends ReactiveCrudRepository<OrbitalBody, Integer> {
  Mono<OrbitalBody> findByNoradId(Integer noradId);
}