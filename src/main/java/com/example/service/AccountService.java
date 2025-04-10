package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account getAccountByUsername(String username){
        return accountRepository.findByUsername(username);
    }

    public void register(Account account){
        accountRepository.save(account);
    }

    public boolean canLogin(Account account){
        if(accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword()) == null){
            return false;
        }
        return true;
    }
}
