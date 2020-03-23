package com.octo.service;

import com.octo.exception.CompteNotExist;
import com.octo.exception.MontantNegatif;
import com.octo.exception.MontantNotExist;
import com.octo.exception.SoldInsuffisant;
import com.octo.model.VirementRequest;
import com.octo.validator.VirementValidator;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class VirementService {

    private final VirementValidator validator;
    private final AccountService accountService;

    public VirementService(VirementValidator virementValidator,AccountService accountService){
        this.validator = virementValidator;
        this.accountService=accountService;
    }


    public void performTransfer(VirementRequest virementRequest) throws Exception {

        if(!validator.validateMontantExist(virementRequest.getMontant()))
            throw new MontantNotExist();

        if(!validator.validateMontantPositif(virementRequest.getMontant()))
            throw new MontantNegatif();

        if(!validator.validateCompteEmetteurExist(virementRequest.getCompteEmetteur()))
            throw new CompteNotExist("Compte Emetteur n'existe pas");

        if (!validator.validateCompteDestinataireExist(virementRequest.getCompteDestinataire()))
            throw new CompteNotExist("Compte Destinataire n'existe pas");

        if(!validator.validateSoldEmetteurSupToMontant(virementRequest.getCompteEmetteur(),virementRequest.getMontant()))
            throw new SoldInsuffisant();


        //Débiter le compte Emetteur
        BigDecimal soldEmetteur = accountService.getBalance(virementRequest.getCompteEmetteur()).subtract(virementRequest.getMontant());
        accountService.setBalance(virementRequest.getCompteEmetteur(),soldEmetteur);

        //Créditer le compte Destinataire
        BigDecimal soldDestinataire = accountService.getBalance(virementRequest.getCompteDestinataire()).add(virementRequest.getMontant());
        accountService.setBalance(virementRequest.getCompteDestinataire(),soldDestinataire);

        System.out.println("Transfert terminé avec succée...");

    }


}
