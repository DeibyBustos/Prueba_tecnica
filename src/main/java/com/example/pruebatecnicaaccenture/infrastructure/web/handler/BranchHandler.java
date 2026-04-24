package com.example.pruebatecnicaaccenture.infrastructure.web.handler;

import com.example.pruebatecnicaaccenture.application.dto.BranchDto;
import com.example.pruebatecnicaaccenture.application.usecase.BranchUseCase;
import com.example.pruebatecnicaaccenture.infrastructure.web.mapper.BranchDtoMapper;
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
public class BranchHandler {

    private final BranchUseCase branchUseCase;
    private final BranchDtoMapper dtoMapper;

    public Mono<ServerResponse> addBranch(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        log.info("ADDING BRANCH TO FRANCHISE WITH ID: ({})", franchiseId);
        return request.bodyToMono(BranchDto.CreateRequest.class)
                .flatMap(body -> {
                    log.info("ADDING BRANCH WITH NAME: ({}) TO FRANCHISE ID: ({})", body.getName(), franchiseId);
                    return branchUseCase.addBranchToFranchise(franchiseId, body.getName());
                })
                .map(dtoMapper::toResponse)
                .flatMap(response -> {
                    log.info("BRANCH CREATED WITH ID: ({}), NAME: ({})", response.getId(), response.getName());
                    return ServerResponse.status(HttpStatus.CREATED).bodyValue(response);
                });
    }

    public Mono<ServerResponse> getBranchesByFranchise(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        log.info("GETTING BRANCHES FOR FRANCHISE WITH ID: ({})", franchiseId);
        return ServerResponse.ok()
                .body(branchUseCase.getBranchesByFranchise(franchiseId)
                        .map(dtoMapper::toResponse), BranchDto.Response.class);
    }

    public Mono<ServerResponse> updateBranchName(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        log.info("UPDATING BRANCH NAME WITH ID: ({})", id);
        return request.bodyToMono(BranchDto.UpdateNameRequest.class)
                .flatMap(body -> {
                    log.info("UPDATING BRANCH ID: ({}) WITH NEW NAME: ({})", id, body.getName());
                    return branchUseCase.updateBranchName(id, body.getName());
                })
                .map(dtoMapper::toResponse)
                .flatMap(response -> {
                    log.info("BRANCH UPDATED ID: ({}), NAME: ({})", response.getId(), response.getName());
                    return ServerResponse.ok().bodyValue(response);
                });
    }
}
