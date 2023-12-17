package com.ar.cac.homebanking.services.implementation;

import com.ar.cac.homebanking.exceptions.AccountNotFoundException;
import com.ar.cac.homebanking.exceptions.UserNotExistsException;
import com.ar.cac.homebanking.mappers.AccountMapper;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.dtos.AccountDTO;
import com.ar.cac.homebanking.repositories.AccountRepository;
import com.ar.cac.homebanking.services.abstraction.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImp implements AccountService {

    private final AccountRepository repository;



    public AccountServiceImp(AccountRepository repository){
        this.repository = repository;

    }
    public Optional<List<AccountDTO>> getAccounts() {
        List<Account> accounts = repository.findAll();
        return Optional.of( accounts.stream()
                .map(AccountMapper::accountToDto)
                .collect(Collectors.toList()));
        /*return accounts.stream()
                .map(AccountMapper::accountToDto)
                .collect(Collectors.toList());*/
    }

    public Optional<AccountDTO> createAccount(AccountDTO dto) {
        // TODO: REFACTOR
        //dto.setType(AccountType.SAVINGS_BANK);
        dto.setAmount(BigDecimal.ZERO);
        Account newAccount = repository.save(AccountMapper.dtoToAccount(dto));
        return Optional.of(AccountMapper.accountToDto(newAccount));
    }

    public Optional<AccountDTO> getAccountById(Long id) {
        return repository.findById(id).map(AccountMapper::accountToDto);

        /*Account entity = repository.findById(id).get();
        return AccountMapper.accountToDto(entity);*/
    }

    public String deleteAccount(Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
            return "La cuenta con id: " + id + " ha sido eliminada";
        } else {
            // TODO: REFACTOR create new exception
            throw new UserNotExistsException("La cuenta a eliminar no existe");
        }

    }

    public Optional<AccountDTO> updateAccount(Long id, AccountDTO dto) {
        Optional<Account> accountOptional = repository.findById(id);
        if (accountOptional.isPresent()) {
            Account existingAccount = accountOptional.get();

            if (dto.getAlias() != null) {
                existingAccount.setAlias(dto.getAlias());
            }

            if (dto.getType() != null) {
                existingAccount.setType(dto.getType());
            }

            if (dto.getCbu() != null) {
                existingAccount.setCbu(dto.getCbu());
            }

            if (dto.getAmount() != null) {
                existingAccount.setAmount(dto.getAmount());
            }

            Account updatedAccount = repository.save(existingAccount);
            return Optional.ofNullable(AccountMapper.accountToDto(updatedAccount));
        } else {
            throw new AccountNotFoundException("Account not found with id: " + id);
        }
    }
    /*

    ------------------------------------------
    public Optional<AccountDTO> updateAccount(Long id, AccountDTO dto) {
        if (repository.existsById(id)) {
            Account accountToModify = repository.findById(id).get();
            // Validar qué datos no vienen en null para setearlos al objeto ya creado

            // Logica del Patch
            if (dto.getAlias() != null) {
                accountToModify.setAlias(dto.getAlias());
            }

            if (dto.getType() != null) {
                accountToModify.setType(dto.getType());
            }

            if (dto.getCbu() != null) {
                accountToModify.setCbu(dto.getCbu());
            }

            if (dto.getAmount() != null) {
                accountToModify.setAmount(dto.getAmount());
            }

            //Account accountModified = repository.save(accountToModify);
            //AccountMapper.accountToDto(accountModified);

            return Optional.of(AccountMapper.accountToDto(repository
                    .save(AccountMapper.dtoToAccount(dto))));
        }
        return Optional.of(new AccountDTO());
    }

     */
}
