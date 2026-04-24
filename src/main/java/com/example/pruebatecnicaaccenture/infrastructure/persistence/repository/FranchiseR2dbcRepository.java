package com.example.pruebatecnicaaccenture.infrastructure.persistence.repository;

import com.example.pruebatecnicaaccenture.infrastructure.persistence.entity.FranchiseEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface FranchiseR2dbcRepository extends R2dbcRepository<FranchiseEntity, Long> {

    @Modifying
    @Query("UPDATE franchises SET name = :name WHERE id = :id")
    Mono<Integer> updateNameById(Long id, String name);
}
