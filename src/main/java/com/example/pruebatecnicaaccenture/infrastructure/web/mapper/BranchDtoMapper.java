package com.example.pruebatecnicaaccenture.infrastructure.web.mapper;

import com.example.pruebatecnicaaccenture.application.dto.BranchDto;
import com.example.pruebatecnicaaccenture.domain.model.Branch;
import org.springframework.stereotype.Component;

@Component
public class BranchDtoMapper {

    public BranchDto.Response toResponse(Branch branch) {
        return BranchDto.Response.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();
    }
}
