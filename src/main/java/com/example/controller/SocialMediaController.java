package com.example.controller;

import com.azul.crs.client.Response;
import com.example.entity.Account;
import com.example.service.AccountService;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;

    @Autowired
    public SocialMediaController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account account){
        if(!account.getUsername().isEmpty() &&
        account.getPassword().length() >= 4 && 
        accountService.getAccountByUsername(account.getUsername()) == null){
            accountService.register(account);
            Account registeredAccount = accountService.getAccountByUsername(account.getUsername());
            return ResponseEntity.ok().body(registeredAccount);
        } else if (accountService.getAccountByUsername(account.getUsername()) != null){
            return ResponseEntity.status(409).build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        if(accountService.canLogin(account)){
            Account loggedAccount = accountService.getAccountByUsername(account.getUsername());
            return ResponseEntity.ok().body(loggedAccount);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }   
    }
}
