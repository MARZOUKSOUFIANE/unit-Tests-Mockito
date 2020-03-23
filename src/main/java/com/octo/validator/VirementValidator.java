package com.octo.validator;


import com.octo.service.AccountService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class VirementValidator {

    private final AccountService accountService;

    public VirementValidator(AccountService accountService) {
        this.accountService=accountService;
    }

    public boolean validateMontantExist(BigDecimal montant){

        return montant!=null;
    }

    public boolean validateMontantPositif(BigDecimal montant){

        return montant.compareTo(BigDecimal.ZERO)>=0;
    }

    public boolean validateMontantInfToSold(BigDecimal montant,BigDecimal sold){

        return montant.compareTo(sold)<0;
    }


    public boolean validateCompteEmetteurExist(String compte){

        return accountService.getCompte(compte)!=null;
    }

    public boolean validateSoldEmetteurSupToMontant(String compte,BigDecimal montant){

        return accountService.getBalance(compte).compareTo(montant)>0;
    }
    
    public boolean validateCompteDestinataireExist(String compte){

        return accountService.getCompte(compte)!=null;
    }
}
