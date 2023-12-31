package com.example.TaskForgeSpring.repository;

import com.example.TaskForgeSpring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByName(String name);

    void deleteUserById(Long id);

    Optional<User> findUserById(Long id);

    User findByEmail(String email);
}
