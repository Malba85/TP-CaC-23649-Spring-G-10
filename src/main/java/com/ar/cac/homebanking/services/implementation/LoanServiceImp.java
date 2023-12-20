package com.ar.cac.homebanking.services.implementation;

import com.ar.cac.homebanking.exceptions.AccountNotFoundException;
import com.ar.cac.homebanking.exceptions.LoanNotFoundException;
import com.ar.cac.homebanking.mappers.LoanMapper;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.Loan;
import com.ar.cac.homebanking.models.dtos.LoanDTO;
import com.ar.cac.homebanking.models.enums.LoanStatus;
import com.ar.cac.homebanking.repositories.AccountRepository;
import com.ar.cac.homebanking.repositories.LoanRepository;
import com.ar.cac.homebanking.services.abstraction.LoanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanServiceImp implements LoanService {

    private final LoanRepository repository;
    private final AccountRepository accountRepository;

    public LoanServiceImp(LoanRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    public Optional<List<LoanDTO>> getLoans() {
        List<Loan> loans = repository.findAll();
        return Optional.of( loans.stream()
                .map(LoanMapper::loanToDto)
                .collect(Collectors.toList()));
    }

    public Optional<LoanDTO> getLoanById(Long id) {
        return Optional.of(repository.findById(id).map(LoanMapper::loanToDto)
                .orElseThrow(() ->
                new LoanNotFoundException("Cuenta no encontrada con id : " + id)));

        /*Loan loan = repository.findById(id).orElseThrow(() ->
                new LoanNotFoundException("Cuenta no encontrada con id : " + id));
        return LoanMapper.loanToDto(loan);*/
    }

    public Optional<LoanDTO> updateLoan(Long id, LoanDTO loanDto) {
        Loan loan = repository.findById(id).orElseThrow(() ->
                new LoanNotFoundException("Cuenta no encontrada con id : " + id));
        Loan updatedLoan = LoanMapper.dtoToLoan(loanDto);
        updatedLoan.setId(loan.getId());
        return Optional.of(LoanMapper.loanToDto(repository.save(updatedLoan)));
    }

    public String deleteLoan(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Se ha eliminado el préstamo";
        } else {
            return "No se ha eliminado el préstamo";
        }
    }

    @Transactional
    public Optional<LoanDTO> createLoan(LoanDTO loanDto) {
        // Comprobar si la cuenta existe
        Account account = accountRepository.findById(loanDto.getUserId())
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada con id : "
                        + loanDto.getUserId()));

        BigDecimal loanAmount = loanDto.getAmount();
        BigDecimal availableAmount = account.getAmount();
        BigDecimal seventyPercent = availableAmount.multiply(BigDecimal.valueOf(0.7));

        // Verificar si el monto del préstamo es menor o igual al 70% del dinero disponible en la cuenta
        if (loanAmount.compareTo(seventyPercent) <= 0) {
            // Si el monto del préstamo es menor o igual al 70%, se aprueba el préstamo
            Loan loan = LoanMapper.dtoToLoan(loanDto);
            loan.setUser(account.getOwner());
            loan.setStatus(LoanStatus.APPROVED); // Establecer el estado del préstamo como APROBADO

            // Guardar el préstamo en la base de datos
            loan = repository.save(loan);

            // Actualizar el monto de la cuenta con el monto del préstamo
            account.setAmount(availableAmount.subtract(loanAmount));
            accountRepository.save(account);

            // Devolver el DTO del préstamo con información detallada
            return Optional.of(LoanMapper.loanToDto(loan));
        } else {
            // Si el monto del préstamo es mayor al 70%, se niega el préstamo
            Loan loan = LoanMapper.dtoToLoan(loanDto);
            loan.setUser(account.getOwner());
            loan.setStatus(LoanStatus.REJECTED); // Establecer el estado del préstamo como RECHAZADO

            // No se guarda el préstamo en la base de datos, ya que fue rechazado

            // Devolver el DTO del préstamo con información de rechazo
            return Optional.of(LoanMapper.loanToDto(loan));
        }
    }
}
