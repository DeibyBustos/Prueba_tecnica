package com.example.pruebatecnicaaccenture.application.usecase;

import com.example.pruebatecnicaaccenture.domain.exception.ResourceNotFoundException;
import com.example.pruebatecnicaaccenture.domain.model.Franchise;
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
public class FranchiseUseCaseTest {


    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private FranchiseUseCase franchiseUseCase;

    private Franchise franchise;

    @BeforeEach
    void setUp() {
        franchise = Franchise.builder()
                .id(1L)
                .name("McDonald's")
                .build();
    }

    @Test
    @DisplayName("Should create franchise successfully")
    void createFranchise_Success() {
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseUseCase.createFranchise("McDonald's"))
                .expectNextMatches(f -> f.getId().equals(1L)
                        && f.getName().equals("McDonald's"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get franchise by id successfully")
    void getFranchiseById_Success() {
        when(franchiseRepository.findById(1L))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseUseCase.getFranchiseById(1L))
                .expectNextMatches(f -> f.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when franchise not found")
    void getFranchiseById_NotFound() {
        when(franchiseRepository.findById(anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier.create(franchiseUseCase.getFranchiseById(99L))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should return all franchises")
    void getAllFranchises_Success() {
        Franchise franchise2 = Franchise.builder().id(2L).name("Subway").build();

        when(franchiseRepository.findAll())
                .thenReturn(Flux.just(franchise, franchise2));

        StepVerifier.create(franchiseUseCase.getAllFranchises())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should update franchise name successfully")
    void updateFranchiseName_Success() {
        Franchise updated = Franchise.builder().id(1L).name("McDonald's Colombia").build();

        when(franchiseRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(franchiseRepository.updateName(1L, "McDonald's Colombia"))
                .thenReturn(Mono.just(updated));

        StepVerifier.create(franchiseUseCase.updateFranchiseName(1L, "McDonald's Colombia"))
                .expectNextMatches(f -> f.getName().equals("McDonald's Colombia"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating name of non-existent franchise")
    void updateFranchiseName_NotFound() {
        when(franchiseRepository.existsById(anyLong())).thenReturn(Mono.just(false));

        StepVerifier.create(franchiseUseCase.updateFranchiseName(99L, "New Name"))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}
