package com.example.pruebatecnicaaccenture.infrastructure.web.mapper;

import com.example.pruebatecnicaaccenture.application.dto.ProductDto;
import com.example.pruebatecnicaaccenture.domain.model.Product;
import com.example.pruebatecnicaaccenture.domain.model.TopProductByBranch;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoMapper {

    public ProductDto.Response toResponse(Product product) {
        return ProductDto.Response.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .build();
    }

    public ProductDto.TopStockResponse toTopResponse(TopProductByBranch top) {
        return ProductDto.TopStockResponse.builder()
                .branchName(top.getBranchName())
                .productName(top.getProductName())
                .stock(top.getStock())
                .build();
    }
}
