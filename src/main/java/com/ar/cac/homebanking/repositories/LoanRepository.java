package com.ar.cac.homebanking.repositories;

import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan,Long> {
}
