package com.example.pruebatecnicaaccenture.infrastructure.repositoryImpl;

import com.example.pruebatecnicaaccenture.domain.model.Branch;
import com.example.pruebatecnicaaccenture.domain.repository.BranchRepository;
import com.example.pruebatecnicaaccenture.infrastructure.persistence.mapper.BranchMapper;
import com.example.pruebatecnicaaccenture.infrastructure.persistence.repository.BranchR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchRepositoryImpl implements BranchRepository {

    private final BranchR2dbcRepository r2dbcRepository;
    private final BranchMapper mapper;

    @Override
    public Mono<Branch> save(Branch branch) {
        return r2dbcRepository.save(mapper.toEntity(branch))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Branch> findById(Long id) {
        return r2dbcRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Branch> updateName(Long id, String newName) {
        return r2dbcRepository.updateNameById(id, newName)
                .then(r2dbcRepository.findById(id))
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId) {
        return r2dbcRepository.findByFranchiseId(franchiseId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return r2dbcRepository.existsById(id);
    }
}
