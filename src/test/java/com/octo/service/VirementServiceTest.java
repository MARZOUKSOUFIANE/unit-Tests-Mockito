package com.octo.service;

import com.octo.entities.Account;
import com.octo.exception.CompteNotExist;
import com.octo.exception.MontantNegatif;
import com.octo.exception.MontantNotExist;
import com.octo.exception.SoldInsuffisant;
import com.octo.model.VirementRequest;
import com.octo.validator.VirementValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class VirementServiceTest {

    @Mock
    VirementValidator validator;
    @Mock
    AccountService accountService;
    @InjectMocks
    VirementService virementService;

    @Test
    public void performTransferMontantNotExistExceptionTest(){

        String compte1="123456789";
        String compte2="1122334455";
        BigDecimal montant=BigDecimal.valueOf(3000);
        VirementRequest virementRequest=new VirementRequest(compte1,compte2,montant);

        when(validator.validateMontantExist(montant)).thenReturn(false);

        assertThatThrownBy(() -> virementService.performTransfer(virementRequest))
                    .isInstanceOf(MontantNotExist.class);
        verify(validator,times(1)).validateMontantExist(montant);
    }

    @Test
    public void performTransferMontantNegatifExceptionTest(){

        String compte1="123456789";
        String compte2="1122334455";
        BigDecimal montant=BigDecimal.valueOf(3000);
        VirementRequest virementRequest=new VirementRequest(compte1,compte2,montant);

        when(validator.validateMontantExist(montant)).thenReturn(true);
        when(validator.validateMontantPositif(montant)).thenReturn(false);

        assertThatThrownBy(() -> virementService.performTransfer(virementRequest))
                .isInstanceOf(MontantNegatif.class);
        verify(validator,times(1)).validateMontantExist(montant);
        verify(validator,times(1)).validateMontantPositif(montant);
    }

    @Test
    public void performTransferCompteNotExistExceptionTest(){

        String compte1="123456789";
        String compte2="123456789";
        BigDecimal montant=BigDecimal.valueOf(3000);
        VirementRequest virementRequest=new VirementRequest(compte1,compte2,montant);

        when(validator.validateMontantExist(montant)).thenReturn(true);
        when(validator.validateMontantPositif(montant)).thenReturn(true);
        when(validator.validateCompteEmetteurExist(compte1)).thenReturn(true);
        when(validator.validateCompteDestinataireExist(compte1)).thenReturn(false);

        assertThatThrownBy(() -> virementService.performTransfer(virementRequest))
                .isInstanceOf(CompteNotExist.class);
        verify(validator,times(1)).validateMontantExist(montant);
        verify(validator,times(1)).validateMontantPositif(montant);
        verify(validator,times(1)).validateCompteEmetteurExist(compte1);
        verify(validator,times(1)).validateCompteDestinataireExist(compte2);
    }

    @Test
    public void performTransferSoldInsuffisantExceptionTest(){

        String compte1="123456789";
        String compte2="123456789";
        BigDecimal montant=BigDecimal.valueOf(3000);
        VirementRequest virementRequest=new VirementRequest(compte1,compte2,montant);

        when(validator.validateMontantExist(montant)).thenReturn(true);
        when(validator.validateMontantPositif(montant)).thenReturn(true);
        when(validator.validateCompteEmetteurExist(compte1)).thenReturn(true);
        when(validator.validateCompteDestinataireExist(compte2)).thenReturn(true);
        when(validator.validateSoldEmetteurSupToMontant(compte1,montant)).thenReturn(false);

        assertThatThrownBy(() -> virementService.performTransfer(virementRequest))
                .isInstanceOf(SoldInsuffisant.class);
        verify(validator,times(1)).validateMontantExist(montant);
        verify(validator,times(1)).validateMontantPositif(montant);
        verify(validator,times(1)).validateCompteEmetteurExist(compte1);
        verify(validator,times(1)).validateCompteDestinataireExist(compte2);
        verify(validator,times(1)).validateSoldEmetteurSupToMontant(compte1,montant);
    }

    @Test
    public void performTransferTest() throws Exception {

        Account compteEmetteur=new Account("123456789",BigDecimal.valueOf(20000));
        Account compteDestinataire=new Account("1122334455",BigDecimal.valueOf(10000));
        BigDecimal montant=BigDecimal.valueOf(1200);
        BigDecimal newSoldEmetteur=compteEmetteur.getSold().subtract(montant);
        BigDecimal newSoldDestinataire=compteDestinataire.getSold().add(montant);
        VirementRequest virementRequest=new VirementRequest(compteEmetteur.getAccountId(),compteDestinataire.getAccountId(),montant);

        when(validator.validateMontantExist(montant)).thenReturn(true);
        when(validator.validateMontantPositif(montant)).thenReturn(true);
        when(validator.validateCompteEmetteurExist(compteEmetteur.getAccountId())).thenReturn(true);
        when(validator.validateCompteDestinataireExist(compteDestinataire.getAccountId())).thenReturn(true);
        when(validator.validateSoldEmetteurSupToMontant(compteEmetteur.getAccountId(),montant)).thenReturn(true);

        when(accountService.getBalance(compteEmetteur.getAccountId())).thenReturn(compteEmetteur.getSold());
        when(accountService.getBalance(compteDestinataire.getAccountId())).thenReturn(compteDestinataire.getSold());

        //Because setBalance is a void method
        doAnswer((i) -> {
            compteEmetteur.setSold(newSoldEmetteur);
            return null;
        }).when(accountService).setBalance(compteEmetteur.getAccountId(),newSoldEmetteur);

        //Because setBalance is a void method
        doAnswer((i) -> {
            compteDestinataire.setSold(newSoldDestinataire);
            return null;
        }).when(accountService).setBalance(compteDestinataire.getAccountId(),newSoldDestinataire);

        virementService.performTransfer(virementRequest);

        verify(validator,times(1)).validateMontantExist(montant);
        verify(validator,times(1)).validateMontantPositif(montant);
        verify(validator,times(1)).validateCompteEmetteurExist(compteEmetteur.getAccountId());
        verify(validator,times(1)).validateCompteDestinataireExist(compteDestinataire.getAccountId());
        verify(validator,times(1)).validateSoldEmetteurSupToMontant(compteEmetteur.getAccountId(),montant);

        verify(accountService,times(1)).getBalance(compteEmetteur.getAccountId());
        verify(accountService,times(1)).setBalance(compteEmetteur.getAccountId(),newSoldEmetteur);
        verify(accountService,times(1)).getBalance(compteDestinataire.getAccountId());
        verify(accountService,times(1)).setBalance(compteDestinataire.getAccountId(),newSoldDestinataire);

        Assert.assertEquals(compteEmetteur.getSold(),newSoldEmetteur);
        Assert.assertEquals(compteDestinataire.getSold(),newSoldDestinataire);
    }
}
