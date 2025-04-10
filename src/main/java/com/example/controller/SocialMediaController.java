package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

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
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
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

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        if(!message.getMessageText().isEmpty() && 
        message.getMessageText().length() < 255 && 
        accountService.getAccountById(message.getPostedBy()) != null){
            Message createdMessage = messageService.createMessage(message);
            return ResponseEntity.ok().body(createdMessage);
        } else {
            return ResponseEntity.status(400).build();
        }
    }
}
