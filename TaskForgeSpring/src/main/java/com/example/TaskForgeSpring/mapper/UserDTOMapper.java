package com.example.TaskForgeSpring.mapper;

import com.example.TaskForgeSpring.model.DTO.UserDTO;
import com.example.TaskForgeSpring.models.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail()
        );
    }
}
