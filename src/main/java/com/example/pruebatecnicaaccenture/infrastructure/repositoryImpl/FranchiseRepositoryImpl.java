package com.example.pruebatecnicaaccenture.infrastructure.repositoryImpl;

import com.example.pruebatecnicaaccenture.domain.model.Franchise;
import com.example.pruebatecnicaaccenture.domain.repository.FranchiseRepository;
import com.example.pruebatecnicaaccenture.infrastructure.persistence.mapper.FranchiseMapper;
import com.example.pruebatecnicaaccenture.infrastructure.persistence.repository.FranchiseR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseRepositoryImpl implements FranchiseRepository {

    private final FranchiseR2dbcRepository r2dbcRepository;
    private final FranchiseMapper mapper;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return r2dbcRepository.save(mapper.toEntity(franchise))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return r2dbcRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> updateName(Long id, String newName) {
        return r2dbcRepository.updateNameById(id, newName)
                .then(r2dbcRepository.findById(id))
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return r2dbcRepository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return r2dbcRepository.existsById(id);
    }
}
