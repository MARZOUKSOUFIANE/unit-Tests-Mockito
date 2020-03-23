package com.octo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @AllArgsConstructor @NoArgsConstructor
public class VirementRequest {

    private String compteEmetteur;
    private String compteDestinataire;
    private BigDecimal montant;
}
