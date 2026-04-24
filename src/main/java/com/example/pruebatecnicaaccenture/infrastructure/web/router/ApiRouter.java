package com.example.pruebatecnicaaccenture.infrastructure.web.router;

import com.example.pruebatecnicaaccenture.infrastructure.web.handler.BranchHandler;
import com.example.pruebatecnicaaccenture.infrastructure.web.handler.FranchiseHandler;
import com.example.pruebatecnicaaccenture.infrastructure.web.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ApiRouter {
    private static final String API = "/api/v1";

    @Bean
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler) {
        return RouterFunctions.route()
                .POST(API + "/franchises",                        handler::createFranchise)
                .GET(API + "/franchises",                         handler::getAllFranchises)
                .PATCH(API + "/franchises/{id}/name",             handler::updateFranchiseName)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> branchRoutes(BranchHandler handler) {
        return RouterFunctions.route()
                .POST(API + "/franchises/{franchiseId}/branches", handler::addBranch)
                .GET(API + "/franchises/{franchiseId}/branches",  handler::getBranchesByFranchise)
                .PATCH(API + "/branches/{id}/name",               handler::updateBranchName)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions.route()
                .POST(API + "/branches/{branchId}/products",           handler::addProduct)
                .DELETE(API + "/products/{id}",                        handler::deleteProduct)
                .PATCH(API + "/products/{id}/stock",                   handler::updateStock)
                .PATCH(API + "/products/{id}/name",                    handler::updateProductName)
                .GET(API + "/franchises/{franchiseId}/top-products",   handler::getTopStockPerBranch)
                .build();
    }
}
