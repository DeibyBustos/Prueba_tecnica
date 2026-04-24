package com.example.pruebatecnicaaccenture.infrastructure.persistence.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("branches")
public class BranchEntity {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("franchise_id")
    private Long franchiseId;
}
