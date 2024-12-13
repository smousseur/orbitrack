package com.smousseur.orbitrack.api.service;

import com.smousseur.orbitrack.api.repository.OrbitalBodyRepository;
import com.smousseur.orbitrack.model.OrbitalBodySearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class OrbitalBodySearchService {
  private final OrbitalBodyRepository repository;

  public Mono<Page<OrbitalBodySearchResponse>> search(
      String name, String type, int page, int size) {
    int offset = page * size;

    String likeName = name != null ? "%" + name + "%" : null;
    Flux<OrbitalBodySearchResponse> search = repository.search(likeName, type, size, offset);
    Mono<Long> count = repository.count(likeName, type);

    return count
        .zipWith(search.collectList())
        .map(tuple -> new PageImpl<>(tuple.getT2(), PageRequest.of(page, size), tuple.getT1()));
  }
}
