package com.ar.cac.homebanking.services.abstraction;

import com.ar.cac.homebanking.models.dtos.LoanDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LoanService {

    Optional<LoanDTO> createLoan (LoanDTO loanDTO);

    Optional<LoanDTO> getLoanById (Long id);

    Optional<List<LoanDTO>> getLoans ();
    String deleteLoan (Long id);

    Optional<LoanDTO> updateLoan (Long id, LoanDTO loanDto);
}
