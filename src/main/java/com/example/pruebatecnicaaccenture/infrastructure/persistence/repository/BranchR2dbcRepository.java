package com.example.pruebatecnicaaccenture.infrastructure.persistence.repository;

import com.example.pruebatecnicaaccenture.infrastructure.persistence.entity.BranchEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchR2dbcRepository extends R2dbcRepository<BranchEntity, Long> {

    Flux<BranchEntity> findByFranchiseId(Long franchiseId);

    @Modifying
    @Query("UPDATE branches SET name = :name WHERE id = :id")
    Mono<Integer> updateNameById(Long id, String name);
}
