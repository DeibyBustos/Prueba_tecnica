package com.example.pruebatecnicaaccenture.infrastructure.persistence.mapper;

import com.example.pruebatecnicaaccenture.domain.model.Franchise;
import com.example.pruebatecnicaaccenture.infrastructure.persistence.entity.FranchiseEntity;
import org.springframework.stereotype.Component;

@Component
public class FranchiseMapper {
    public Franchise toDomain(FranchiseEntity entity) {
        return Franchise.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public FranchiseEntity toEntity(Franchise domain) {
        return FranchiseEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .build();
    }
}
