package com.ar.cac.homebanking.services.implementation;

import com.ar.cac.homebanking.exceptions.AccountNotFoundException;
import com.ar.cac.homebanking.exceptions.UserNotFoundException;
import com.ar.cac.homebanking.mappers.AccountMapper;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.User;
import com.ar.cac.homebanking.models.dtos.AccountDTO;
import com.ar.cac.homebanking.repositories.AccountRepository;
import com.ar.cac.homebanking.repositories.UserRepository;
import com.ar.cac.homebanking.services.abstraction.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImp implements AccountService {

    private final AccountRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public AccountServiceImp(UserRepository userRepository, AccountRepository repository) {
        this.repository = repository;
        this.userRepository = userRepository;
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

    @Override
    public Optional<AccountDTO> createAccount(AccountDTO dto) {
        if (dto.getUserId() != null) {
            Optional<User> optionalUser = userRepository.findById(dto.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                Account newAccount = new Account();
                newAccount.setType(dto.getType());
                newAccount.setCbu(dto.getCbu());
                newAccount.setAlias(dto.getAlias());
                newAccount.setAmount(BigDecimal.ZERO); // Inicializar con 0
                newAccount.setOwner(user); // Establecer la relación con el usuario

                Account savedAccount = repository.save(newAccount);
                return Optional.of(AccountMapper.accountToDto(savedAccount));
            } else {
                throw new UserNotFoundException("Usuario no encontrado con ID: " + dto.getUserId());
            }
        } else {
            throw new IllegalArgumentException("El ID de usuario (userId) no puede ser nulo");
        }
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
            throw new UserNotFoundException("La cuenta a eliminar no existe");
        }

    }

    public Optional<AccountDTO> updateAccount(Long id, AccountDTO dto) {
        Optional<Account> accountOptional = repository.findById(id);
        if (accountOptional.isPresent()) {
            Account existingAccount = accountOptional.get();

            // Actualizar solo los campos que se proporcionan en el DTO
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

            // Guardar la cuenta actualizada en la base de datos
            Account updatedAccount = repository.save(existingAccount);

            // Devolver DTO correspondiente a la cuenta actualizada
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
