package com.example.pruebatecnicaaccenture.domain.repository;

import com.example.pruebatecnicaaccenture.domain.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(Long id);
    Mono<Franchise> updateName(Long id, String newName);
    Flux<Franchise> findAll();
    Mono<Boolean> existsById(Long id);
}
