package com.example.TaskForgeSpring.model.DTO;

import com.example.TaskForgeSpring.models.Role;
import com.example.TaskForgeSpring.models.Task;

import java.util.Collection;

public record NewUserDTO(
        long id,
        String name,
        String surname,
        String email,
        String password,
        Collection<String> roles
        ) {
    public String getName(){
        return this.name;
    }
    public String getSurname(){
        return this.surname;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPassword(){
        return this.password;
    }
    public Collection<String> getRoles() {
        return this.roles;
    }
}
