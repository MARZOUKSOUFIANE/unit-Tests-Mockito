package com.octo.dao;

import com.octo.entities.Account;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AccountRepositoryImpl implements AccountRepository{

    Map<String,Account> accounts;

    @PostConstruct
    public void loadAccount(){
        accounts=new HashMap<>();
        accounts.put("12345",new Account("12345",new BigDecimal(5000)));
        accounts.put("123456",new Account("12345",new BigDecimal(10000)));
        accounts.put("123457",new Account("12345",new BigDecimal(15000)));
    }


    @Override
    public Account getCompte(String account) {
            return accounts.get(account);
    }
}
