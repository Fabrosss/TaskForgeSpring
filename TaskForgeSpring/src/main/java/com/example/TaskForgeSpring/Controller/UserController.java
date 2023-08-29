package com.example.TaskForgeSpring.Controller;


import com.example.TaskForgeSpring.CustomCorsConfigAnnotation;
import com.example.TaskForgeSpring.exception.ErrorProvidedDataHandler;
import com.example.TaskForgeSpring.model.DTO.NewUserDTO;
import com.example.TaskForgeSpring.model.DTO.UserDTO;
import com.example.TaskForgeSpring.models.User;
import com.example.TaskForgeSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CustomCorsConfigAnnotation
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;
    private final UserService userService;
    @Autowired
    HttpSession httpSession ;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers () {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<UserDTO> geUserById(@PathVariable("id") Long id) {
        UserDTO user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> body ) {
        return userService.loginToService(body.get("email"), body.get("password"));
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = userService.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updateUser = userService.updateUser(user);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/logout")
    public Object logout() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof User) {
            username = ((User)principal).getName();
        }
        else {
            username = principal.toString();
        }
        java.util.Date date = new java.util.Date();
        System.out.println("["+date+"]"+"[USER]: " + username + " logged out from service");
        ErrorProvidedDataHandler errorProvidedDataHandler  = new ErrorProvidedDataHandler();
        errorProvidedDataHandler.setError("2001");
        httpSession.invalidate();
        return errorProvidedDataHandler;
    }
    @PutMapping("/newUser")
    public ResponseEntity<UserDTO> newUser (@RequestBody NewUserDTO user){
        userService.createNewUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
