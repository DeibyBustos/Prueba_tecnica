package com.example.pruebatecnicaaccenture.domain.repository;

import com.example.pruebatecnicaaccenture.domain.model.Product;
import com.example.pruebatecnicaaccenture.domain.model.TopProductByBranch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Mono<Product> findById(Long id);
    Mono<Product> updateStock(Long id, Integer newStock);
    Mono<Product> updateName(Long id, String newName);
    Mono<Void> deleteById(Long id);
    Flux<Product> findByBranchId(Long branchId);
    Flux<TopProductByBranch> findTopStockProductPerBranchByFranchiseId(Long franchiseId);
    Mono<Boolean> existsById(Long id);
}
