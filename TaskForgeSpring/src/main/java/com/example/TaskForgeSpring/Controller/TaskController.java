package com.example.TaskForgeSpring.Controller;

import com.example.TaskForgeSpring.CustomCorsConfigAnnotation;
import com.example.TaskForgeSpring.model.DTO.TaskDTO;
import com.example.TaskForgeSpring.models.Task;
import com.example.TaskForgeSpring.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collection;

@RestController
@RequestMapping("/task")
@CustomCorsConfigAnnotation

public class TaskController {
    private final TaskService taskService;
    @Autowired
    HttpSession httpSession ;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/getTask/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id){
        TaskDTO taskDTO = taskService.getTaskById(id);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }
    @GetMapping("/user/{id}/{status}")
    public ResponseEntity<Collection<TaskDTO>> getTaskByUserStatus(
            @PathVariable("id") Long id,
            @PathVariable("status") String status){
        Collection<TaskDTO> tasks = taskService.getUserTaskByStatus(id, status);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<Collection<TaskDTO>> getTaskByUser(
            @PathVariable("id") Long id
            ){
        Collection<TaskDTO> tasks = taskService.getUserTask(id);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    @PutMapping("/edit")
    public ResponseEntity<Task> editTask(@RequestBody Task task){
        Task taskEdited = taskService.editTask(task);
        return new ResponseEntity<>(taskEdited, HttpStatus.OK);
    }

}
