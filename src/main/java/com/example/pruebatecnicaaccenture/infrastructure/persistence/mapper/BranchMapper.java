package com.example.pruebatecnicaaccenture.infrastructure.persistence.mapper;

import com.example.pruebatecnicaaccenture.domain.model.Branch;
import com.example.pruebatecnicaaccenture.infrastructure.persistence.entity.BranchEntity;
import org.springframework.stereotype.Component;

@Component
public class BranchMapper {

    public Branch toDomain(BranchEntity entity) {
        return Branch.builder()
                .id(entity.getId())
                .name(entity.getName())
                .franchiseId(entity.getFranchiseId())
                .build();
    }

    public BranchEntity toEntity(Branch domain) {
        return BranchEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .franchiseId(domain.getFranchiseId())
                .build();
    }
}
