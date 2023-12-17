package com.ar.cac.homebanking.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "mail")
    private String email;
    @Column(name = "contrasena")
    private String password;
    @Column(name = "nombre")
    private String name;
    @Column(name = "apellido")
    private String surname;
    private String dni;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transfer> transfers;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans;
    /*
    CascadeType: Indica qué acciones deben ser propagadas de la entidad principal a las entidades relacionadas
    cuando se realiza una operación de persistencia en un objeto todas las operaciones asociadas también se
    propagarán.
    orphanRemoval: Esta propiedad se relaciona específicamente con las entidades huérfanas, es decir, aquellas
    que ya no tienen una referencia desde la entidad principal.
     */
}
