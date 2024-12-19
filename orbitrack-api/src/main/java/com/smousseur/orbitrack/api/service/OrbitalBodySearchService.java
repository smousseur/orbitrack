package com.smousseur.orbitrack.api.service;

import com.smousseur.orbitrack.api.model.OrbitalBodySearchResponse;
import com.smousseur.orbitrack.api.model.PageResult;
import com.smousseur.orbitrack.api.repository.OrbitalBodyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class OrbitalBodySearchService {
  private final OrbitalBodyRepository repository;

  public Mono<PageResult<OrbitalBodySearchResponse>> search(
      Integer noradId,
      String name,
      String cosparId,
      String type,
      String countryCode,
      int page,
      int size) {
    int offset = page * size;

    String likeName = "%" + name + "%";
    Flux<OrbitalBodySearchResponse> search =
        repository.search(noradId, likeName, cosparId, type, countryCode, size, offset);
    Mono<Integer> count = repository.count(noradId, likeName, cosparId, type, countryCode);

    return count
        .zipWith(search.collectList())
        .map(tuple -> new PageResult<>(tuple.getT2(), tuple.getT1()));
  }
}
