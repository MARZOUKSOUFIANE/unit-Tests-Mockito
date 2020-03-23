package com.octo.dao;

import com.octo.entities.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository {

    Account getCompte(String account);
}
