package com.ar.cac.homebanking.controllers;

import com.ar.cac.homebanking.models.dtos.LoanDTO;
import com.ar.cac.homebanking.models.dtos.LoanDTO;
import com.ar.cac.homebanking.models.dtos.LoanDTO;
import com.ar.cac.homebanking.models.dtos.LoanDTO;
import com.ar.cac.homebanking.services.implementation.LoanServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanServiceImp service;

    public LoanController(LoanServiceImp service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<LoanDTO>> getLoans(){
        Optional<List<LoanDTO>> LoanListOptional = service.getLoans();

        if(LoanListOptional.isPresent()){
            List<LoanDTO> LoanList = LoanListOptional.get();
            return new ResponseEntity<>(LoanList, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
        //ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LoanDTO> getLoanById(@PathVariable Long id){
        return service.getLoanById(id).map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@RequestBody LoanDTO dto){
        return service.createLoan(dto).map(t -> new ResponseEntity<>(t, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.CONFLICT));
        //ResponseEntity.status(HttpStatus.CREATED).body(service.createLoan(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<LoanDTO> updateLoan(@PathVariable Long id, @RequestBody LoanDTO dto){
        return service.updateLoan(id, dto).map(t -> new ResponseEntity<>(t, HttpStatus.ACCEPTED))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteLoan(@PathVariable Long id){
        return new ResponseEntity<>(service.deleteLoan(id), HttpStatus.OK);
    }
}