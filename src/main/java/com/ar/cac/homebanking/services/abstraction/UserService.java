package com.ar.cac.homebanking.services.abstraction;

import com.ar.cac.homebanking.exceptions.NoUsersFoundException;
import com.ar.cac.homebanking.exceptions.UserAlreadyExistsException;
import com.ar.cac.homebanking.exceptions.UserNotFoundException;
import com.ar.cac.homebanking.models.User;
import com.ar.cac.homebanking.models.dtos.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    Optional<UserDTO> createUser(UserDTO userDTO) throws UserAlreadyExistsException;

    Optional<UserDTO> getUserById(Long id) throws UserNotFoundException;

    Optional<List<UserDTO>> getUsers() throws NoUsersFoundException;;

    String deleteUser(Long id) throws UserNotFoundException;

    Optional<UserDTO> updateUser(Long id, UserDTO userDto) throws UserNotFoundException;

    User validateUserByEmail(UserDTO dto) throws UserNotFoundException;
}
