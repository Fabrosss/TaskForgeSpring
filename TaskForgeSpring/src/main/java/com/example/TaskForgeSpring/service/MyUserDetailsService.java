package com.example.TaskForgeSpring.service;


import com.example.TaskForgeSpring.models.Role;
import com.example.TaskForgeSpring.models.User;
import com.example.TaskForgeSpring.repository.RoleRepository;
import com.example.TaskForgeSpring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service("userDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(mail);
        if (user == null) {
            log.info("User of email {}, not found", mail);
            return new org.springframework.security.core.userdetails.User("", "",true, true, true, true, Collections.emptyList());


        }
        log.info("User of email {} logged in ", mail );
        org.springframework.security.core.userdetails.User mojamama = new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), true, true, true, true, getAuthorities(user.getRoles()));
        log.info("Moja mama to: {}", mojamama);
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), true, true, true, true, getAuthorities(user.getRoles()));
    }
    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return getGrantedAuthorities(getRoles(roles));
    }

    private List<String> getRoles (Collection<Role> roles) {
        List<String> newRoles = new ArrayList<>();
        for (Role role : roles) {
            newRoles.add(role.getName());
        }
        return newRoles;
    }
    private List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        System.out.println(roles);
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}