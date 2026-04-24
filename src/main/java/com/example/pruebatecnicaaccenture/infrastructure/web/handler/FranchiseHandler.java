package com.example.pruebatecnicaaccenture.infrastructure.web.handler;

import com.example.pruebatecnicaaccenture.application.dto.FranchiseDto;
import com.example.pruebatecnicaaccenture.application.usecase.FranchiseUseCase;
import com.example.pruebatecnicaaccenture.infrastructure.web.mapper.FranchiseDtoMapper;
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
public class FranchiseHandler {

    private final FranchiseUseCase franchiseUseCase;
    private final FranchiseDtoMapper dtoMapper;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        log.info("CREATING NEW FRANCHISE");
        return request.bodyToMono(FranchiseDto.CreateRequest.class)
                .flatMap(body -> {
                    log.info("CREATING FRANCHISE WITH NAME: ({})", body.getName());
                    return franchiseUseCase.createFranchise(body.getName());
                })
                .map(dtoMapper::toResponse)
                .flatMap(response -> {
                    log.info("FRANCHISE CREATED WITH ID: ({}), NAME: ({})", response.getId(), response.getName());
                    return ServerResponse.status(HttpStatus.CREATED).bodyValue(response);
                });
    }

    public Mono<ServerResponse> getAllFranchises(ServerRequest request) {
        log.info("GETTING ALL FRANCHISES");
        return ServerResponse.ok()
                .body(franchiseUseCase.getAllFranchises()
                        .map(dtoMapper::toResponse), FranchiseDto.Response.class);
    }

    public Mono<ServerResponse> updateFranchiseName(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        log.info("UPDATING FRANCHISE NAME WITH ID: ({})", id);
        return request.bodyToMono(FranchiseDto.UpdateNameRequest.class)
                .flatMap(body -> {
                    log.info("UPDATING FRANCHISE ID: ({}) WITH NEW NAME: ({})", id, body.getName());
                    return franchiseUseCase.updateFranchiseName(id, body.getName());
                })
                .map(dtoMapper::toResponse)
                .flatMap(response -> {
                    log.info("FRANCHISE UPDATED ID: ({}), NAME: ({})", response.getId(), response.getName());
                    return ServerResponse.ok().bodyValue(response);
                });
    }
}
