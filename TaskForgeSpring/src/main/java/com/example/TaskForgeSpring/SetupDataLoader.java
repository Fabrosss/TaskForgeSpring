package com.example.TaskForgeSpring;


import com.example.TaskForgeSpring.models.Privilege;
import com.example.TaskForgeSpring.models.Role;
import com.example.TaskForgeSpring.models.User;
import com.example.TaskForgeSpring.repository.PrivilegeRepository;
import com.example.TaskForgeSpring.repository.RoleRepository;
import com.example.TaskForgeSpring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

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

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        createUserIfNotFound("Test", Arrays.asList(adminRole));

        this.alreadySetup = true;

    }

    @Transactional
    public User createUserIfNotFound(String name, List<Role> roles) {
        User user = userRepository.findByName(name);
        if (user == null) {
            user = new User()
                    .setName(name)
                    .setSurname("Test")
                    .setRoles(roles)
                    .setPassword(passwordEncoder.encode("test"))
                    .setEmail("test@test.pl");
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
}
