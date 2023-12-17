package com.ar.cac.homebanking.mappers;

import com.ar.cac.homebanking.models.Loan;
import com.ar.cac.homebanking.models.User;
import com.ar.cac.homebanking.models.dtos.LoanDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LoanMapper {

    public Loan dtoToLoan(LoanDTO dto) {
        return Loan.builder()
                .amount(dto.getAmount())
                .interestRate(dto.getInterestRate())
                .duration(dto.getDuration())
                .status(dto.getStatus())
                .user(dto.getUserId() != null ? User.builder().id(dto.getUserId()).build() : null)
                .build();
    }

    public LoanDTO loanToDto(Loan loan) {
        return LoanDTO.builder()
                .id(loan.getId())
                .amount(loan.getAmount())
                .interestRate(loan.getInterestRate())
                .duration(loan.getDuration())
                .status(loan.getStatus())
                .userId(loan.getUser() != null ? loan.getUser().getId() : null)
                .build();
    }
}