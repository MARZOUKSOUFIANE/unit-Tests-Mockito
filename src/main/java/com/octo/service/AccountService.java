package com.octo.service;

import com.octo.entities.Account;
import com.octo.dao.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public Account getCompte(String compte){
        return accountRepository.getCompte(compte);
    }

    public BigDecimal getBalance(String compte){
        return getCompte(compte).getSold();
    }

    public void setBalance(String compte,BigDecimal montant){
        BigDecimal prevSold=getCompte(compte).getSold();
        getCompte(compte).setSold(prevSold.add(montant));
    }

}
