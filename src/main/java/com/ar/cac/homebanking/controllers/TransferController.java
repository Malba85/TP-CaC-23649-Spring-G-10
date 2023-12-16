package com.ar.cac.homebanking.controllers;

import com.ar.cac.homebanking.models.dtos.TransferDTO;
import com.ar.cac.homebanking.models.dtos.TransferDTO;
import com.ar.cac.homebanking.services.implementation.TransferServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferServiceImp service;

    public TransferController(TransferServiceImp service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TransferDTO>> getTransfers(){
        Optional<List<TransferDTO>> TransferListOptional = service.getTransfers();

        if(TransferListOptional.isPresent()){
            List<TransferDTO> TransferList = TransferListOptional.get();
            return new ResponseEntity<>(TransferList, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TransferDTO> getTransferById(@PathVariable Long id){
        return service.getTransferById(id).map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping
    public ResponseEntity<TransferDTO> performTransfer(@RequestBody TransferDTO dto){
       return service.performTransfer(dto).map(t -> new ResponseEntity<>(t, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TransferDTO> updateTransfer(@PathVariable Long id, @RequestBody TransferDTO dto){
        return service.updateTransfer(id, dto).map(t -> new ResponseEntity<>(t, HttpStatus.ACCEPTED))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteTransfer(@PathVariable Long id){
        return new ResponseEntity<>(service.deleteTransfer(id), HttpStatus.OK);
    }
}
