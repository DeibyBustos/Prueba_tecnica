package com.example.pruebatecnicaaccenture.domain.repository;

import com.example.pruebatecnicaaccenture.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {
    Mono<Branch> save(Branch branch);
    Mono<Branch> findById(Long id);
    Mono<Branch> updateName(Long id, String newName);
    Flux<Branch> findByFranchiseId(Long franchiseId);
    Mono<Boolean> existsById(Long id);
}
