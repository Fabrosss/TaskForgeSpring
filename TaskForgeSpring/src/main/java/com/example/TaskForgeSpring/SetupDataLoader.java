package com.example.TaskForgeSpring;


import com.example.TaskForgeSpring.models.Privilege;
import com.example.TaskForgeSpring.models.Role;
import com.example.TaskForgeSpring.models.Task;
import com.example.TaskForgeSpring.models.User;
import com.example.TaskForgeSpring.repository.PrivilegeRepository;
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
import java.util.Arrays;
import java.util.Collection;
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
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup)
            return;

        Privilege readPrivilege
                = createPrivilageIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilageIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege, writePrivilege));
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

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        createUserIfNotFound("Test", Arrays.asList(adminRole), tasks);

        this.alreadySetup = true;

    }

    @Transactional
    public User createUserIfNotFound(String name, List<Role> roles, List<Task> tasks) {
        User user = userRepository.findByName(name);
        if (user == null) {
            user = new User()
                    .setName(name)
                    .setSurname("Test")
                    .setRoles(roles)
                    .setPassword(passwordEncoder.encode("test"))
                    .setEmail("test@test.pl")
                    .setTasks(tasks);
            userRepository.save(user);
        }
        return user;
    }

    @Transactional
    public Privilege createPrivilageIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if(privilege == null){
            privilege = new Privilege()
                    .setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    public Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges){
        Role role = roleRepository.findByName(name);
        if(role == null) {
            role = new Role()
                    .setName(name)
                    .setPrivileges(privileges);
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

