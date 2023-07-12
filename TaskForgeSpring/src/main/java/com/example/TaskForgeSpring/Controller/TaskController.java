package com.example.TaskForgeSpring.Controller;

import com.example.TaskForgeSpring.CustomCorsConfigAnnotation;
import com.example.TaskForgeSpring.model.DTO.TaskDTO;
import com.example.TaskForgeSpring.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/task")
@CustomCorsConfigAnnotation

public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Collection<TaskDTO>> getTaskByUser(@PathVariable("id") Long id){
        Collection<TaskDTO> tasks = taskService.getUserTask(id);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

}
