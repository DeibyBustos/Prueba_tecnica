package com.example.pruebatecnicaaccenture.infrastructure.repositoryImpl;

import com.example.pruebatecnicaaccenture.domain.model.Product;
import com.example.pruebatecnicaaccenture.domain.model.TopProductByBranch;
import com.example.pruebatecnicaaccenture.domain.repository.ProductRepository;
import com.example.pruebatecnicaaccenture.infrastructure.persistence.mapper.ProductMapper;
import com.example.pruebatecnicaaccenture.infrastructure.persistence.repository.ProductR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductR2dbcRepository r2dbcRepository;
    private final ProductMapper mapper;
    private final DatabaseClient databaseClient;

    @Override
    public Mono<Product> save(Product product) {
        return r2dbcRepository.save(mapper.toEntity(product))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return r2dbcRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> updateStock(Long id, Integer newStock) {
        return r2dbcRepository.updateStockById(id, newStock)
                .then(r2dbcRepository.findById(id))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> updateName(Long id, String newName) {
        return r2dbcRepository.updateNameById(id, newName)
                .then(r2dbcRepository.findById(id))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return r2dbcRepository.deleteById(id);
    }

    @Override
    public Flux<Product> findByBranchId(Long branchId) {
        return r2dbcRepository.findByBranchId(branchId)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<TopProductByBranch> findTopStockProductPerBranchByFranchiseId(Long franchiseId) {
        String sql = """
                SELECT b.name AS branch_name, p.name AS product_name, p.stock
                FROM products p
                JOIN branches b ON p.branch_id = b.id
                WHERE b.franchise_id = :franchiseId
                  AND p.stock = (
                      SELECT MAX(p2.stock)
                      FROM products p2
                      WHERE p2.branch_id = b.id
                  )
                ORDER BY b.name
                """;

        return databaseClient.sql(sql)
                .bind("franchiseId", franchiseId)
                .map((row, metadata) -> TopProductByBranch.builder()
                        .branchName(row.get("branch_name", String.class))
                        .productName(row.get("product_name", String.class))
                        .stock(row.get("stock", Integer.class))
                        .build())
                .all();
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return r2dbcRepository.existsById(id);
    }
}
