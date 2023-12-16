package com.ar.cac.homebanking.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String dni;

    private String name;

    private String surname;

    private String password;

    private String email;

}
