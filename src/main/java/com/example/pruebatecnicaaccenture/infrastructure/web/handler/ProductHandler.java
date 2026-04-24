package com.example.pruebatecnicaaccenture.infrastructure.web.handler;

import com.example.pruebatecnicaaccenture.application.dto.ProductDto;
import com.example.pruebatecnicaaccenture.application.usecase.ProductUseCase;
import com.example.pruebatecnicaaccenture.infrastructure.web.mapper.ProductDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductUseCase productUseCase;
    private final ProductDtoMapper dtoMapper;

    public Mono<ServerResponse> addProduct(ServerRequest request) {
        Long branchId = Long.valueOf(request.pathVariable("branchId"));
        log.info("ADDING PRODUCT TO BRANCH WITH ID: ({})", branchId);
        return request.bodyToMono(ProductDto.CreateRequest.class)
                .flatMap(body -> {
                    log.info("ADDING PRODUCT WITH NAME: ({}) AND STOCK: ({}) TO BRANCH ID: ({})", body.getName(), body.getStock(), branchId);
                    return productUseCase.addProductToBranch(branchId, body.getName(), body.getStock());
                })
                .map(dtoMapper::toResponse)
                .flatMap(response -> {
                    log.info("PRODUCT CREATED WITH ID: ({}), NAME: ({}), STOCK: ({})", response.getId(), response.getName(), response.getStock());
                    return ServerResponse.status(HttpStatus.CREATED).bodyValue(response);
                });
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        log.info("DELETING PRODUCT WITH ID: ({})", id);
        return productUseCase.deleteProduct(id)
                .flatMap(unused -> {
                    log.info("PRODUCT DELETED WITH ID: ({})", id);
                    return ServerResponse.noContent().build();
                })
                .switchIfEmpty(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        log.info("UPDATING STOCK FOR PRODUCT WITH ID: ({})", id);
        return request.bodyToMono(ProductDto.UpdateStockRequest.class)
                .flatMap(body -> {
                    log.info("UPDATING PRODUCT ID: ({}) WITH NEW STOCK: ({})", id, body.getStock());
                    return productUseCase.updateProductStock(id, body.getStock());
                })
                .map(dtoMapper::toResponse)
                .flatMap(response -> {
                    log.info("STOCK UPDATED PRODUCT ID: ({}), NEW STOCK: ({})", response.getId(), response.getStock());
                    return ServerResponse.ok().bodyValue(response);
                });
    }

    public Mono<ServerResponse> updateProductName(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        log.info("UPDATING NAME FOR PRODUCT WITH ID: ({})", id);
        return request.bodyToMono(ProductDto.UpdateNameRequest.class)
                .flatMap(body -> {
                    log.info("UPDATING PRODUCT ID: ({}) WITH NEW NAME: ({})", id, body.getName());
                    return productUseCase.updateProductName(id, body.getName());
                })
                .map(dtoMapper::toResponse)
                .flatMap(response -> {
                    log.info("PRODUCT NAME UPDATED ID: ({}), NEW NAME: ({})", response.getId(), response.getName());
                    return ServerResponse.ok().bodyValue(response);
                });
    }

    public Mono<ServerResponse> getTopStockPerBranch(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        log.info("GETTING TOP STOCK PRODUCTS PER BRANCH FOR FRANCHISE WITH ID: ({})", franchiseId);
        return ServerResponse.ok()
                .body(productUseCase.getTopStockProductPerBranch(franchiseId)
                        .map(dtoMapper::toTopResponse), ProductDto.TopStockResponse.class);
    }
}
