package com.ar.cac.homebanking.services.abstraction;

import com.ar.cac.homebanking.models.User;
import com.ar.cac.homebanking.models.dtos.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    Optional<UserDTO> createUser (UserDTO userDTO);

    Optional<UserDTO> getUserById (Long id);

    Optional<List<UserDTO>> getUsers ();
    String deleteUser (Long id);

    Optional<UserDTO> updateUser (Long id, UserDTO userDto);

    User validateUserByEmail(UserDTO dto);
}
