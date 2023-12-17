package com.ar.cac.homebanking.controllers;

import com.ar.cac.homebanking.models.dtos.AccountDTO;
import com.ar.cac.homebanking.services.abstraction.AccountService;
import com.ar.cac.homebanking.services.implementation.AccountServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountServiceImp service){
        this.accountService = service;

    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccounts(){
        Optional<List<AccountDTO>> accountListOptional = accountService.getAccounts();

        if(accountListOptional.isPresent()){
            List<AccountDTO> accountList = accountListOptional.get();
            return new ResponseEntity<>(accountList, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
                //ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id){
        return accountService.getAccountById(id).map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                //ResponseEntity.status(HttpStatus.OK).body(service.getAccountById(id));
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO dto) {
        Long userId = dto.getUserId(); // Obtener el ID del usuario desde el DTO de la cuenta

        // Crear la cuenta y asignarla al usuario con el ID proporcionado
        Optional<AccountDTO> createdAccount = accountService.createAccount(dto);

        return createdAccount
                .map(account -> new ResponseEntity<>(account, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long id, @RequestBody AccountDTO dto){
        return accountService.updateAccount(id, dto).map(t -> new ResponseEntity<>(t, HttpStatus.ACCEPTED))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        //ResponseEntity.status(HttpStatus.OK).body(service.updateAccount(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        return new ResponseEntity<>(accountService.deleteAccount(id), HttpStatus.OK);
        //ResponseEntity.status(HttpStatus.OK).body(service.deleteAccount(id));
    }
}
