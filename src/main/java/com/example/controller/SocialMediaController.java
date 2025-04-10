package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.ok().body(messageService.getAllMessages());
    }

    @GetMapping("messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int message_id){
        return ResponseEntity.ok().body(messageService.getMessageById(message_id));
    }

    @DeleteMapping("messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int message_id){
        if(messageService.getMessageById(message_id) != null){
            messageService.deleteMessageById(message_id);
            return ResponseEntity.ok().body(1);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("messages/{message_id}")
    public ResponseEntity<Integer> updateMessageById(@RequestBody Message message, @PathVariable int message_id){
        if(messageService.getMessageById(message_id) != null &&
        !message.getMessageText().isEmpty() && 
        message.getMessageText().length() < 255){
            message.setMessageId(message_id);
            messageService.updateMessage(message);
            return ResponseEntity.ok().body(1);
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByAccountId(@PathVariable int account_id){
        return ResponseEntity.ok().body(messageService.getAllMessagesByAccountId(account_id));
    }
}
