package com.example.pruebatecnicaaccenture.infrastructure.web.mapper;

import com.example.pruebatecnicaaccenture.application.dto.FranchiseDto;
import com.example.pruebatecnicaaccenture.domain.model.Franchise;
import org.springframework.stereotype.Component;

@Component
public class FranchiseDtoMapper {
    public FranchiseDto.Response toResponse(Franchise franchise) {
        return FranchiseDto.Response.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }
}
