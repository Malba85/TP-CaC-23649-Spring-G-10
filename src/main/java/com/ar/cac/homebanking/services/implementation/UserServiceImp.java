package com.ar.cac.homebanking.services.implementation;

import com.ar.cac.homebanking.exceptions.NoUsersFoundException;
import com.ar.cac.homebanking.exceptions.UserAlreadyExistsException;
import com.ar.cac.homebanking.exceptions.UserNotFoundException;
import com.ar.cac.homebanking.mappers.UserMapper;
import com.ar.cac.homebanking.models.User;
import com.ar.cac.homebanking.models.dtos.UserDTO;
import com.ar.cac.homebanking.repositories.UserRepository;
import com.ar.cac.homebanking.services.abstraction.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public Optional<List<UserDTO>> getUsers() throws NoUsersFoundException {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            throw new NoUsersFoundException("No se encontraron usuarios en la base de datos.");
        }
        return Optional.of(users.stream().map(UserMapper::userToDto).collect(Collectors.toList()));
    }

    @Override
    public Optional<UserDTO> createUser(UserDTO userDto) throws UserAlreadyExistsException {
        if (validateUserByEmail(userDto) == null) {
            User user = UserMapper.dtoToUser(userDto);
            User savedUser = repository.save(user);
            return Optional.of(UserMapper.userToDto(savedUser));
        } else {
            throw new UserAlreadyExistsException("Usuario con mail: " + userDto.getEmail() + " ya existe");
        }
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) throws UserNotFoundException {
        User user = repository.findById(id).orElseThrow(() ->
                new UserNotFoundException("Usuario con ID: " + id + " no encontrado"));
        return Optional.of(UserMapper.userToDto(user));
    }

    @Override
    public String deleteUser(Long id) throws UserNotFoundException {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "El usuario con ID: " + id + " ha sido eliminado";
        } else {
            throw new UserNotFoundException("El usuario a eliminar con ID: " + id + " no existe");
        }
    }

    @Override
    public Optional<UserDTO> updateUser(Long id, UserDTO dto) throws UserNotFoundException {
        Optional<User> optionalUser = repository.findById(id);

        if (optionalUser.isPresent()) {
            User userToUpdate = optionalUser.get();

            // Verificar si los campos del DTO son no nulos y actualizar los correspondientes en el usuario existente
            if (dto.getName() != null) {
                userToUpdate.setName(dto.getName());
            }
            if (dto.getSurname() != null) {
                userToUpdate.setSurname(dto.getSurname());
            }
            if (dto.getEmail() != null) {
                userToUpdate.setEmail(dto.getEmail());
            }
            if (dto.getPassword() != null) {
                userToUpdate.setPassword(dto.getPassword());
            }
            if (dto.getDni() != null) {
                userToUpdate.setDni(dto.getDni());
            }

            // Guardar el usuario actualizado en la base de datos
            User updatedUser = repository.save(userToUpdate);

            return Optional.of(UserMapper.userToDto(updatedUser));
        } else {
            throw new UserNotFoundException("Usuario con ID: " + id + " no encontrado");
        }
    }

    @Override
    public User validateUserByEmail(UserDTO dto) {
        return repository.findByEmail(dto.getEmail());
    }
}

