package com.octo.validator;

import com.octo.entities.Account;
import com.octo.service.AccountService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest

public class VirementValidatorTest {

    @Mock
    AccountService accountService;
    @InjectMocks
    VirementValidator virementValidator;


    @Test
    public void validateMontantExistTest(){

        BigDecimal montant=BigDecimal.valueOf(1500);
        Assert.assertTrue(virementValidator.validateMontantExist(montant));
    }

    @Test
    public void validateMontantNotExistTest(){

        Assert.assertFalse(virementValidator.validateMontantExist(null));
    }

    @Test
    public void validateMontantPositifTest(){

        BigDecimal montant=BigDecimal.valueOf(1500);
        Assert.assertTrue(virementValidator.validateMontantPositif(montant));
    }

    @Test
    public void validateMontantNotPositifTest(){

        BigDecimal montant=BigDecimal.valueOf(-1500);
        Assert.assertFalse(virementValidator.validateMontantPositif(montant));
    }

    @Test
    public void validateMontantInfToSoldTest(){

        BigDecimal montant=BigDecimal.valueOf(3700);
        BigDecimal solde=BigDecimal.valueOf(20000);
        Assert.assertTrue(virementValidator.validateMontantInfToSold(montant,solde));
    }

    @Test
    public void validateMontantNotInfToSoldTest(){

        BigDecimal montant=BigDecimal.valueOf(3700);
        BigDecimal solde=BigDecimal.valueOf(1000);
        Assert.assertFalse(virementValidator.validateMontantInfToSold(montant,solde));
    }

    @Test
    public void validateCompteEmetteurExistTest(){

        String compte= "123456789";
        when(accountService.getCompte(compte)).thenReturn(new Account(compte,BigDecimal.valueOf(20000)));
        Assert.assertTrue(virementValidator.validateCompteEmetteurExist(compte));
        verify(accountService,times(1)).getCompte(compte);

    }

    @Test
    public void validateCompteEmetteurNotExistTest(){

        String compte= "123456789";
        when(accountService.getCompte(compte)).thenReturn(null);
        Assert.assertFalse(virementValidator.validateCompteEmetteurExist(compte));
        verify(accountService,times(1)).getCompte(compte);

    }

    @Test
    public void validateSoldEmetteurSupToMontantTest(){

        String compte= "123456789";
        BigDecimal montant=BigDecimal.valueOf(1500);
        when(accountService.getBalance(compte)).thenReturn(BigDecimal.valueOf(17000));
        Assert.assertTrue(virementValidator.validateSoldEmetteurSupToMontant(compte,montant));
        verify(accountService,times(1)).getBalance(compte);

    }

    @Test
    public void validateSoldEmetteurNotSupToMontantTest(){

        String compte= "123456789";
        BigDecimal montant=BigDecimal.valueOf(3700);
        when(accountService.getBalance(compte)).thenReturn(BigDecimal.valueOf(2500));
        Assert.assertFalse(virementValidator.validateSoldEmetteurSupToMontant(compte,montant));
        verify(accountService,times(1)).getBalance(compte);

    }

    @Test
    public void validateCompteDestinataireExistTest(){

        String compte= "123456789";
        when(accountService.getCompte(compte)).thenReturn(new Account(compte,BigDecimal.valueOf(20000)));
        Assert.assertTrue(virementValidator.validateCompteDestinataireExist(compte));
        verify(accountService,times(1)).getCompte(compte);

    }

    @Test
    public void validateCompteDestinataireNotExistTest(){

        String compte= "123456789";
        when(accountService.getCompte(compte)).thenReturn(null);
        Assert.assertFalse(virementValidator.validateCompteDestinataireExist(compte));
        verify(accountService,times(1)).getCompte(compte);

    }
}
