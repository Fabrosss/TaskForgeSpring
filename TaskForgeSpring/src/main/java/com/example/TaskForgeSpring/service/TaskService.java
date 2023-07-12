package com.example.TaskForgeSpring.service;

import com.example.TaskForgeSpring.exception.UserNotFoundException;
import com.example.TaskForgeSpring.mapper.TaskDTOMapper;
import com.example.TaskForgeSpring.model.DTO.TaskDTO;
import com.example.TaskForgeSpring.models.Task;
import com.example.TaskForgeSpring.models.User;
import com.example.TaskForgeSpring.repository.TaskRepository;
import com.example.TaskForgeSpring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskDTOMapper taskDTOMapper;

    @Autowired
    public TaskService(
            TaskRepository taskRepository,
            UserRepository userRepository,
            TaskDTOMapper taskDTOMapper
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskDTOMapper = taskDTOMapper;
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(long taskId) {
        taskRepository.deleteTaskById(taskId);
    }

    public Collection<TaskDTO> getUserTask(long userID) {
        User user = this.userRepository.findUserById(userID).orElseThrow(
                () -> new UserNotFoundException("User by id " + userID + " was not found"));
        Collection<Task> tasks = user.getTasks(); // Pobierz listę zadań z użytkownika

        Collection<TaskDTO> taskDTOs = tasks.stream()
                .map(taskDTOMapper::apply) // Mapowanie na TaskDTO za pomocą taskDTOMapper
                .collect(Collectors.toList()); // Zebranie wyników do listy

        return taskDTOs;
    }
}