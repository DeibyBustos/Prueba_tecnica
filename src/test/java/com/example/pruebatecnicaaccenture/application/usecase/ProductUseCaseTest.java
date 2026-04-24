package com.example.pruebatecnicaaccenture.application.usecase;

import com.example.pruebatecnicaaccenture.domain.exception.ResourceNotFoundException;
import com.example.pruebatecnicaaccenture.domain.model.Product;
import com.example.pruebatecnicaaccenture.domain.model.TopProductByBranch;
import com.example.pruebatecnicaaccenture.domain.repository.BranchRepository;
import com.example.pruebatecnicaaccenture.domain.repository.FranchiseRepository;
import com.example.pruebatecnicaaccenture.domain.repository.ProductRepository;
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
public class ProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private ProductUseCase productUseCase;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Big Mac")
                .stock(150)
                .branchId(1L)
                .build();
    }

    @Test
    @DisplayName("Should add product to branch successfully")
    void addProductToBranch_Success() {
        when(branchRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(productUseCase.addProductToBranch(1L, "Big Mac", 150))
                .expectNextMatches(p -> p.getId().equals(1L)
                        && p.getName().equals("Big Mac")
                        && p.getStock().equals(150)
                        && p.getBranchId().equals(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when branch not found")
    void addProductToBranch_BranchNotFound() {
        when(branchRepository.existsById(anyLong())).thenReturn(Mono.just(false));

        StepVerifier.create(productUseCase.addProductToBranch(99L, "Big Mac", 150))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should delete product successfully")
    void deleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(productRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.deleteProduct(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent product")
    void deleteProduct_NotFound() {
        when(productRepository.existsById(anyLong())).thenReturn(Mono.just(false));

        StepVerifier.create(productUseCase.deleteProduct(99L))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should update product stock successfully")
    void updateProductStock_Success() {
        Product updated = Product.builder().id(1L).name("Big Mac").stock(999).branchId(1L).build();

        when(productRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(productRepository.updateStock(1L, 999)).thenReturn(Mono.just(updated));

        StepVerifier.create(productUseCase.updateProductStock(1L, 999))
                .expectNextMatches(p -> p.getStock().equals(999))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating stock of non-existent product")
    void updateProductStock_NotFound() {
        when(productRepository.existsById(anyLong())).thenReturn(Mono.just(false));

        StepVerifier.create(productUseCase.updateProductStock(99L, 999))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should update product name successfully")
    void updateProductName_Success() {
        Product updated = Product.builder().id(1L).name("Big Mac Doble").stock(150).branchId(1L).build();

        when(productRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(productRepository.updateName(1L, "Big Mac Doble")).thenReturn(Mono.just(updated));

        StepVerifier.create(productUseCase.updateProductName(1L, "Big Mac Doble"))
                .expectNextMatches(p -> p.getName().equals("Big Mac Doble"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return top stock product per branch")
    void getTopStockProductPerBranch_Success() {
        TopProductByBranch top1 = TopProductByBranch.builder()
                .branchName("Bogotá Centro")
                .productName("Big Mac")
                .stock(999)
                .build();
        TopProductByBranch top2 = TopProductByBranch.builder()
                .branchName("Medellín El Poblado")
                .productName("Quarter Pounder")
                .stock(200)
                .build();

        when(franchiseRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(productRepository.findTopStockProductPerBranchByFranchiseId(1L))
                .thenReturn(Flux.just(top1, top2));

        StepVerifier.create(productUseCase.getTopStockProductPerBranch(1L))
                .expectNextMatches(t -> t.getBranchName().equals("Bogotá Centro")
                        && t.getStock().equals(999))
                .expectNextMatches(t -> t.getBranchName().equals("Medellín El Poblado")
                        && t.getStock().equals(200))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when franchise not found for top products")
    void getTopStockProductPerBranch_FranchiseNotFound() {
        when(franchiseRepository.existsById(anyLong())).thenReturn(Mono.just(false));

        StepVerifier.create(productUseCase.getTopStockProductPerBranch(99L))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}
