package com.ar.cac.homebanking.mappers;

import com.ar.cac.homebanking.models.Transfer;
import com.ar.cac.homebanking.models.dtos.TransferDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransferMapper {

    public Transfer dtoToTransfer(TransferDTO dto){
        return Transfer.builder()
                .amount(dto.getAmount())
                .date(dto.getDate())
                .originAccount(dto.getOriginAccount() != null ? AccountMapper.dtoToAccount(dto.getOriginAccount()) : null)
                .targetAccount(dto.getTargetAccount() != null ? AccountMapper.dtoToAccount(dto.getTargetAccount()) : null)
                .build();
    }

    public TransferDTO transferToDto(Transfer transfer){
        return TransferDTO.builder()
                .id(transfer.getId())
                .amount(transfer.getAmount())
                .originAccount(transfer.getOriginAccount() != null ? AccountMapper.accountToDto(transfer.getOriginAccount()) : null)
                .targetAccount(transfer.getTargetAccount() != null ? AccountMapper.accountToDto(transfer.getTargetAccount()) : null)
                .date(transfer.getDate())
                .build();
    }
}