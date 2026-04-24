package com.example.pruebatecnicaaccenture.application.usecase;


import com.example.pruebatecnicaaccenture.domain.exception.ResourceNotFoundException;
import com.example.pruebatecnicaaccenture.domain.model.Branch;
import com.example.pruebatecnicaaccenture.domain.repository.BranchRepository;
import com.example.pruebatecnicaaccenture.domain.repository.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BranchUseCaseTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private BranchUseCase branchUseCase;

    private Branch branch;

    @BeforeEach
    void setUp() {
        branch = Branch.builder()
                .id(1L)
                .name("Bogotá Centro")
                .franchiseId(1L)
                .build();
    }

    @Test
    @DisplayName("Should add branch to franchise successfully")
    void addBranchToFranchise_Success() {
        when(franchiseRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(branch));

        StepVerifier.create(branchUseCase.addBranchToFranchise(1L, "Bogotá Centro"))
                .expectNextMatches(b -> b.getId().equals(1L)
                        && b.getName().equals("Bogotá Centro")
                        && b.getFranchiseId().equals(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when franchise does not exist")
    void addBranchToFranchise_FranchiseNotFound() {
        when(franchiseRepository.existsById(anyLong())).thenReturn(Mono.just(false));

        StepVerifier.create(branchUseCase.addBranchToFranchise(99L, "Bogotá Centro"))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should return branches by franchise")
    void getBranchesByFranchise_Success() {
        Branch branch2 = Branch.builder().id(2L).name("Medellín").franchiseId(1L).build();

        when(branchRepository.findByFranchiseId(1L))
                .thenReturn(Flux.just(branch, branch2));

        StepVerifier.create(branchUseCase.getBranchesByFranchise(1L))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should update branch name successfully")
    void updateBranchName_Success() {
        Branch updated = Branch.builder().id(1L).name("Bogotá Chapinero").franchiseId(1L).build();

        when(branchRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(branchRepository.updateName(1L, "Bogotá Chapinero"))
                .thenReturn(Mono.just(updated));

        StepVerifier.create(branchUseCase.updateBranchName(1L, "Bogotá Chapinero"))
                .expectNextMatches(b -> b.getName().equals("Bogotá Chapinero"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when branch not found")
    void updateBranchName_NotFound() {
        when(branchRepository.existsById(anyLong())).thenReturn(Mono.just(false));

        StepVerifier.create(branchUseCase.updateBranchName(99L, "New Name"))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}
