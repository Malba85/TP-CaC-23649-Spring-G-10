package com.ar.cac.homebanking.mappers;

import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.dtos.AccountDTO;
import lombok.experimental.UtilityClass;


@UtilityClass
public class AccountMapper {

    public AccountDTO accountToDto(Account account){
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setType(account.getType());
        dto.setCbu(account.getCbu());
        dto.setAlias(account.getAlias());
        dto.setAmount(account.getAmount());
        dto.setUserId(account.getOwner().getId());
        // Puedes mapear otras propiedades si es necesario
        return dto;
    }

    public Account dtoToAccount(AccountDTO dto){
        Account account = new Account();
        account.setId(dto.getId());
        account.setType(dto.getType());
        account.setCbu(dto.getCbu());
        account.setAlias(dto.getAlias());
        account.setAmount(dto.getAmount());
        // Puedes mapear otras propiedades si es necesario
        return account;
    }
}
