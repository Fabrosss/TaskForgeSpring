package com.example.TaskForgeSpring.model.DTO;

import com.example.TaskForgeSpring.models.Role;
import com.example.TaskForgeSpring.models.Task;

import java.util.Collection;

public record UserDTO(
        long id,
        String name,
        String surname,
        String email,
        Collection<Role> roles,
        Collection<Task> tasks
){

}
