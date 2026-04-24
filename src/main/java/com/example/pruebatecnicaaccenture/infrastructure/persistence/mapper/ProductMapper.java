package com.example.pruebatecnicaaccenture.infrastructure.persistence.mapper;

import com.example.pruebatecnicaaccenture.domain.model.Product;
import com.example.pruebatecnicaaccenture.domain.model.TopProductByBranch;
import com.example.pruebatecnicaaccenture.infrastructure.persistence.entity.ProductEntity;
import com.example.pruebatecnicaaccenture.infrastructure.persistence.repository.ProductR2dbcRepository.TopStockProjection;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toDomain(ProductEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .stock(entity.getStock())
                .branchId(entity.getBranchId())
                .build();
    }

    public ProductEntity toEntity(Product domain) {
        return ProductEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .stock(domain.getStock())
                .branchId(domain.getBranchId())
                .build();
    }

    public TopProductByBranch projectionToDomain(TopStockProjection projection) {
        return TopProductByBranch.builder()
                .branchName(projection.getBranchName())
                .productName(projection.getProductName())
                .stock(projection.getStock())
                .build();
    }
}
