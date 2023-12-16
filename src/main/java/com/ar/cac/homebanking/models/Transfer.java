package com.ar.cac.homebanking.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long id;
    private Date date;
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "origin_account_id")
    private Account originAccount;
    @ManyToOne
    @JoinColumn(name = "target_account_id")
    private Account targetAccount;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
