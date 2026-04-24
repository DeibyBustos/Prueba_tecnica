package com.example.pruebatecnicaaccenture.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopProductByBranch {
    private String branchName;
    private String productName;
    private Integer stock;
}
