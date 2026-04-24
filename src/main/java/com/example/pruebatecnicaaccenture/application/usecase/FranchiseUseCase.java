package com.example.pruebatecnicaaccenture.application.usecase;

import com.example.pruebatecnicaaccenture.domain.exception.ResourceNotFoundException;
import com.example.pruebatecnicaaccenture.domain.model.Franchise;
import com.example.pruebatecnicaaccenture.domain.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> createFranchise(String name) {
        return franchiseRepository.save(Franchise.builder().name(name).build());
    }

    public Mono<Franchise> getFranchiseById(Long id) {
        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franchise", id)));
    }

    public Flux<Franchise> getAllFranchises() {
        return franchiseRepository.findAll();
    }

    public Mono<Franchise> updateFranchiseName(Long id, String newName) {
        return franchiseRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.error(new ResourceNotFoundException("Franchise", id));
                    return franchiseRepository.updateName(id, newName);
                });
    }
}
