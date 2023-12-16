package com.ar.cac.homebanking.controllers;

import com.ar.cac.homebanking.models.dtos.UserDTO;
import com.ar.cac.homebanking.models.dtos.UserDTO;
import com.ar.cac.homebanking.services.implementation.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // Generar una instancia del Service (UserServiceImp) -> Inyectar una instancia mediante Spring Boot
    @Autowired
    private final UserServiceImp service;

    public UserController(UserServiceImp service){
        this.service = service;
    }

    // Para cada método HTTP permitido debemos realizar una acción
    // Definir el DTO y el Service (Inyección de Dependencia)

    // CRUD: Crear, Leer, Modificar, Eliminar


    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(){
        Optional<List<UserDTO>> UsersListOptional = service.getUsers();

        if(UsersListOptional.isPresent()){
            List<UserDTO> UserList = UsersListOptional.get();
            return new ResponseEntity<>(UserList, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return service.getUserById(id).map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto){
        return service.createUser(dto).map(t -> new ResponseEntity<>(t, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO dto){
        return service.updateUser(id, dto).map(t -> new ResponseEntity<>(t, HttpStatus.ACCEPTED))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return new ResponseEntity<>(service.deleteUser(id), HttpStatus.OK);
    }

}
