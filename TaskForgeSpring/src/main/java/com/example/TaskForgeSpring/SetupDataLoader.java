package com.example.TaskForgeSpring;


import com.example.TaskForgeSpring.models.Role;
import com.example.TaskForgeSpring.models.Task;
import com.example.TaskForgeSpring.models.User;
import com.example.TaskForgeSpring.repository.RoleRepository;
import com.example.TaskForgeSpring.repository.TaskRepository;
import com.example.TaskForgeSpring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup)
            return;


        createRoleIfNotFound("ADMIN");
        createRoleIfNotFound("USER");

        createTaskIfNotFound(
                "Implementacja interfejsu",
                "Napisać klasę implementującą interfejs do zarządzania danymi",
                8L,
                LocalDate.of(2023,7,8),
                LocalDate.of(2023,7,15)
                );
        createTaskIfNotFound(
                "Optymalizacja algorytmu sortowania",
                "Znaleźć sposób na zoptymalizowanie sortowania złożoności O(n^2)",
                4L,
                LocalDate.of(2023,7,12),
                LocalDate.of(2023,7,18)
                );
        createTaskIfNotFound("Tworzenie aplikacji mobilnej",
                "Rozpocząć rozwój aplikacji mobilnej przy użyciu frameworka Flutter",
                8L,
                LocalDate.of(2023,7,13),
                LocalDate.of(2023,7,21)
                );
        List<Task> tasks = taskRepository.findAll();
        Role adminRole = roleRepository.findByName("ADMIN");
        Role userRole = roleRepository.findByName("USER");
        Task task2 = taskRepository.findByTopic("Optymalizacja algorytmu sortowania");

        List<Task> taskList = new ArrayList<>();
        if (task2 != null) {
            taskList.add(task2);
        }
        createUserIfNotFound("Test", "test@test.pl", Arrays.asList(adminRole), tasks);
        createUserIfNotFound("edytor", "edytor@test.pl", Arrays.asList(userRole), taskList);
        this.alreadySetup = true;
    }

    @Transactional
    public User createUserIfNotFound(String name, String email, List<Role> roles, List<Task> tasks) {
        User user = userRepository.findByName(name);
        if (user == null) {
            user = new User()
                    .setName(name)
                    .setSurname("Test")
                    .setRoles(roles)
                    .setPassword(passwordEncoder.encode("test"))
                    .setEmail(email)
                    .setTasks(tasks);
            userRepository.save(user);
        }
        return user;
    }



    @Transactional
    public Role createRoleIfNotFound(
            String name){
        Role role = roleRepository.findByName(name);
        if(role == null) {
            role = new Role()
                    .setName(name);
            roleRepository.save(role);
        }
        return role;
    }
    @Transactional
    public Task createTaskIfNotFound(String topic, String dsc, Long hours, LocalDate startingDate, LocalDate endingDate){
        Task task = new Task()
                .setTopic(topic)
                .setDescription(dsc)
                .setHours(hours)
                .setStartingDate(startingDate)
                .setEndingDate(endingDate);
        taskRepository.save(task);
        return task;
    }
}

