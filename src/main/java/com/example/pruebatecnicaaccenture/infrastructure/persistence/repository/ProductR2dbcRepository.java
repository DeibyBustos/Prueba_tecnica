package com.example.pruebatecnicaaccenture.infrastructure.persistence.repository;

import com.example.pruebatecnicaaccenture.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductR2dbcRepository extends R2dbcRepository<ProductEntity, Long> {

    Flux<ProductEntity> findByBranchId(Long branchId);

    @Modifying
    @Query("UPDATE products SET stock = :stock WHERE id = :id")
    Mono<Integer> updateStockById(Long id, Integer stock);

    @Modifying
    @Query("UPDATE products SET name = :name WHERE id = :id")
    Mono<Integer> updateNameById(Long id, String name);

    @Query("""
        SELECT b.name AS branchName, p.name AS productName, p.stock
        FROM products p
        JOIN branches b ON p.branch_id = b.id
        WHERE b.franchise_id = :franchiseId
          AND p.stock = (
              SELECT MAX(p2.stock)
              FROM products p2
              WHERE p2.branch_id = b.id
          )
        ORDER BY b.name
        """)
    Flux<TopStockProjection> findTopStockPerBranchByFranchiseId(Long franchiseId);

    interface TopStockProjection {
        String getBranchName();
        String getProductName();
        Integer getStock();
    }
}
