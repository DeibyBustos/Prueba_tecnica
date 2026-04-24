package com.example.pruebatecnicaaccenture.application.usecase;

import com.example.pruebatecnicaaccenture.domain.exception.ResourceNotFoundException;
import com.example.pruebatecnicaaccenture.domain.model.Branch;
import com.example.pruebatecnicaaccenture.domain.repository.BranchRepository;
import com.example.pruebatecnicaaccenture.domain.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BranchUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Branch> addBranchToFranchise(Long franchiseId, String name) {
        return franchiseRepository.existsById(franchiseId)
                .flatMap(exists -> {
                    if (!exists) return Mono.error(new ResourceNotFoundException("Franchise", franchiseId));
                    return branchRepository.save(Branch.builder()
                            .name(name)
                            .franchiseId(franchiseId)
                            .build());
                });
    }

    public Flux<Branch> getBranchesByFranchise(Long franchiseId) {
        return branchRepository.findByFranchiseId(franchiseId);
    }

    public Mono<Branch> updateBranchName(Long id, String newName) {
        return branchRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.error(new ResourceNotFoundException("Branch", id));
                    return branchRepository.updateName(id, newName);
                });
    }
}
