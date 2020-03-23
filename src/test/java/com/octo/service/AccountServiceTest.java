package com.octo.service;

import com.octo.dao.AccountRepository;
import com.octo.entities.Account;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.InstanceOf;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AccountServiceTest {


    @Mock
    AccountRepository accountRepository;
    @InjectMocks
    AccountService accountService;

    @Test
    public void getCompteTest(){
        String compte="1122334455";
        when(accountRepository.getCompte(compte)).thenReturn(new Account(compte,BigDecimal.valueOf(1200)));

        Assert.assertThat(accountService.getCompte(compte), instanceOf(Account.class));
        verify(accountRepository,times(1)).getCompte(compte);

    }

    @Test
    public void getBalanceTest(){
        String compte="1122334455";
        when(accountRepository.getCompte(compte)).thenReturn(new Account(compte,BigDecimal.valueOf(1200)));

        Assert.assertEquals(accountService.getBalance(compte), BigDecimal.valueOf(1200));
    }

    @Test
    public void setBalanceTest(){
        String compteId="1122334455";
        Account compte=new Account(compteId,BigDecimal.valueOf(1200));
        when(accountRepository.getCompte(compteId)).thenReturn(compte);

        accountService.setBalance(compteId,BigDecimal.valueOf(1000));

        Assert.assertEquals(compte.getSold(),BigDecimal.valueOf(2200));
    }



}
