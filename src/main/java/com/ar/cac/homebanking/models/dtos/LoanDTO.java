package com.ar.cac.homebanking.models.dtos;

import com.ar.cac.homebanking.models.enums.LoanStatus;
import lombok.*;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoanDTO {
    private Long id;
    private BigDecimal amount;
    private double interestRate;
    private int duration;
    private LoanStatus status;
    private Long userId;

}