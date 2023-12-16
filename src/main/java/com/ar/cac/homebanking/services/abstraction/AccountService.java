package com.ar.cac.homebanking.services.abstraction;

import com.ar.cac.homebanking.models.dtos.AccountDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {

    Optional<AccountDTO> createAccount (AccountDTO accountDTO);

    Optional<AccountDTO> getAccountById (Long id);

    Optional<List<AccountDTO>> getAccounts ();
    String deleteAccount (Long id);

    Optional<AccountDTO> updateAccount (Long id, AccountDTO accountDto);
}
