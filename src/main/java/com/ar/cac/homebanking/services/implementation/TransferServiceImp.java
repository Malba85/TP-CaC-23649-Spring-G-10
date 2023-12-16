package com.ar.cac.homebanking.services.implementation;

import com.ar.cac.homebanking.exceptions.AccountNotFoundException;
import com.ar.cac.homebanking.exceptions.InsufficientFoundsException;
import com.ar.cac.homebanking.exceptions.TransferNotFoundException;
import com.ar.cac.homebanking.mappers.TransferMapper;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.Transfer;
import com.ar.cac.homebanking.models.dtos.TransferDTO;
import com.ar.cac.homebanking.repositories.AccountRepository;
import com.ar.cac.homebanking.repositories.TransferRepository;
import com.ar.cac.homebanking.services.abstraction.TransferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransferServiceImp implements TransferService {

    private final TransferRepository repository;

    private final AccountRepository accountRepository;

    public TransferServiceImp(TransferRepository repository, AccountRepository accountRepository){
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    public Optional<List<TransferDTO>> getTransfers(){
        List<Transfer> transfers = repository.findAll();
        return Optional.of(transfers.stream()
                .map(TransferMapper::transferToDto)
                .collect(Collectors.toList()));
    }

    public Optional<TransferDTO> getTransferById(Long id){
        return Optional.of(repository.findById(id).map(TransferMapper::transferToDto)
                .orElseThrow(() ->
                        new TransferNotFoundException("Transfer with id" + id + " not found")));

        /*Transfer transfer = repository.findById(id).orElseThrow(() ->
                new TransferNotFoundException("Transfer not found with id: " + id));
        return TransferMapper.transferToDto(transfer);*/
    }

    public Optional<TransferDTO> updateTransfer(Long id, TransferDTO transferDto){
        Transfer transfer = repository.findById(id).orElseThrow(() ->
                new TransferNotFoundException("Transfer not found with id: " + id));
        Transfer updatedTransfer = TransferMapper.dtoToTransfer(transferDto);
        updatedTransfer.setId(transfer.getId());
        return Optional.of(TransferMapper.transferToDto(repository.save(updatedTransfer)));
    }

    public String deleteTransfer(Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
            return "Se ha eliminado la transferencia";
        } else {
            return "No se ha eliminado la transferencia";
        }
    }
    @Transactional
    public Optional<TransferDTO> performTransfer(TransferDTO dto) {
        // Comprobar si las cuentas de origen y destino existen
        Account originAccount = accountRepository.findById(dto.getOriginAccount().getId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: "
                        + dto.getOriginAccount().getId()));
        Account destinationAccount = accountRepository.findById(dto.getTargetAccount().getId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: "
                        + dto.getTargetAccount().getId()));

        // Comprobar si la cuenta de origen tiene fondos suficientes
        if (originAccount.getAmount().compareTo(dto.getAmount()) < 0) {
            throw new InsufficientFoundsException("Insufficient funds in the account with id: "
                    + dto.getOriginAccount().getId());
        }

        // Realizar la transferencia
        originAccount.setAmount(originAccount.getAmount().subtract(dto.getAmount()));
        destinationAccount.setAmount(destinationAccount.getAmount().add(dto.getAmount()));

        // Guardar las cuentas actualizadas
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        // Crear la transferencia y guardarla en la base de datos
        Transfer transfer = new Transfer();
        Date date = new Date();
        transfer.setDate(date);
        transfer.setOriginAccount(originAccount);
        transfer.setTargetAccount(destinationAccount);
        transfer.setAmount(dto.getAmount());
        transfer = repository.save(transfer);

        // Devuelve el DTO de la transferencia con información detallada de las cuentas
        return Optional.of(TransferMapper.transferToDto(transfer));
    }
}
