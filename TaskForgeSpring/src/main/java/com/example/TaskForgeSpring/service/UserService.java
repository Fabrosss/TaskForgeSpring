package com.example.TaskForgeSpring.service;


import com.example.TaskForgeSpring.exception.ErrorProvidedDataHandler;
import com.example.TaskForgeSpring.exception.UserNotFoundException;
import com.example.TaskForgeSpring.mapper.UserDTOMapper;
import com.example.TaskForgeSpring.models.Role;
import com.example.TaskForgeSpring.models.User;
import com.example.TaskForgeSpring.repository.RoleRepository;
import com.example.TaskForgeSpring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserDTOMapper userDTOMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    private final static String USER_NOT_FOUND = "user with email &s not found";
    @Autowired
    public UserService(UserDTOMapper userDTOMapper, UserRepository userRepository, RoleRepository roleRepository) {
        this.userDTOMapper = userDTOMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User addUser(User user){
        Role role = roleRepository.findByName("ADMIN");
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public User findUserByUserName(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name);
        if(user == null){
            throw new UsernameNotFoundException("User not found " + name);
        }
        return user;
    }
    public User findUserById(Long id){
        return userRepository.findUserById(id).orElseThrow(
                () -> new UserNotFoundException("User by id " + id + " was not found"));
    }
    public void deleteUser(Long id){
        userRepository.deleteEmployeeById(id);
    }

    public Object loginToService(String userMail, String password) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();

        User user = userRepository.findByEmail(userMail);
        if (user == null) {
            errorProvidedDataHandler.setError("3028");
            return errorProvidedDataHandler;
        }
        else {
            if (passwordEncoder.matches(password, user.getPassword())) {
                java.util.Date date = new java.util.Date();
                myUserDetailsService.loadUserByUsername(userMail);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String currentPrincipalName =  authentication.getName();
                System.out.println("["+date+"]"+"[USER]: " + currentPrincipalName + " logged into service");
                return userDTOMapper.apply(user);
            }
            else {
                errorProvidedDataHandler.setError("3029");
                return errorProvidedDataHandler;
            }
        }

    }
}

