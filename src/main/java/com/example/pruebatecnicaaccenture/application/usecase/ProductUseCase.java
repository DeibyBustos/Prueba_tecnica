package com.example.pruebatecnicaaccenture.application.usecase;

import com.example.pruebatecnicaaccenture.domain.exception.ResourceNotFoundException;
import com.example.pruebatecnicaaccenture.domain.model.Product;
import com.example.pruebatecnicaaccenture.domain.model.TopProductByBranch;
import com.example.pruebatecnicaaccenture.domain.repository.BranchRepository;
import com.example.pruebatecnicaaccenture.domain.repository.FranchiseRepository;
import com.example.pruebatecnicaaccenture.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Product> addProductToBranch(Long branchId, String name, Integer stock) {
        return branchRepository.existsById(branchId)
                .flatMap(exists -> {
                    if (!exists) return Mono.error(new ResourceNotFoundException("Branch", branchId));
                    return productRepository.save(Product.builder()
                            .name(name)
                            .stock(stock)
                            .branchId(branchId)
                            .build());
                });
    }

    public Mono<Void> deleteProduct(Long productId) {
        return productRepository.existsById(productId)
                .flatMap(exists -> {
                    if (!exists) return Mono.error(new ResourceNotFoundException("Product", productId));
                    return productRepository.deleteById(productId);
                });
    }

    public Mono<Product> updateProductStock(Long productId, Integer newStock) {
        return productRepository.existsById(productId)
                .flatMap(exists -> {
                    if (!exists) return Mono.error(new ResourceNotFoundException("Product", productId));
                    return productRepository.updateStock(productId, newStock);
                });
    }

    public Mono<Product> updateProductName(Long productId, String newName) {
        return productRepository.existsById(productId)
                .flatMap(exists -> {
                    if (!exists) return Mono.error(new ResourceNotFoundException("Product", productId));
                    return productRepository.updateName(productId, newName);
                });
    }

    public Flux<TopProductByBranch> getTopStockProductPerBranch(Long franchiseId) {
        return franchiseRepository.existsById(franchiseId)
                .flatMapMany(exists -> {
                    if (!exists) return Flux.error(new ResourceNotFoundException("Franchise", franchiseId));
                    return productRepository.findTopStockProductPerBranchByFranchiseId(franchiseId);
                });
    }
}
