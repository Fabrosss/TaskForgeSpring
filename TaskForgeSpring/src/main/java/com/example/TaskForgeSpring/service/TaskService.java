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

import javax.transaction.Transactional;
import java.time.LocalDate;

import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
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
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }
    @Transactional
    public void deleteTask(long taskId) {
        Task taskToDelete = taskRepository.findById(taskId);
        if (taskToDelete != null) {
            for (User user : taskToDelete.getUsers()) {
                user.getTasks().remove(taskToDelete);
            }
            taskRepository.delete(taskToDelete);
        }
    }

    public Collection<TaskDTO> getUserTask(long userID) {
        User user = this.userRepository.findUserById(userID).orElseThrow(
                () -> new UserNotFoundException("User by id " + userID + " was not found"));
        Collection<Task> tasks = user.getTasks();
        Collection<TaskDTO> taskDTOs = tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());

        return taskDTOs;
    }
    public Collection<TaskDTO> getAllTasksByStatus(String status){
        Collection<Task> tasks = taskRepository.findAll();
        LocalDate today = LocalDate.now();

        Collection<TaskDTO> taskDTOs = switch (status) {
            case "present" -> tasks.stream()
                    .filter(task -> !task.getStartingDate().isAfter(today) &&
                            !task.getEndingDate().isBefore(today))
                    .map(taskDTOMapper)
                    .collect(Collectors.toList());
            case "past" -> tasks.stream()
                    .filter(task -> task.getEndingDate().isBefore(today))
                    .map(taskDTOMapper)
                    .collect(Collectors.toList());
            case "future" -> tasks.stream()
                    .filter(task -> task.getStartingDate().isAfter(today))
                    .map(taskDTOMapper)
                    .collect(Collectors.toList());
            default -> tasks.stream()
                    .map(taskDTOMapper)
                    .collect(Collectors.toList());
        };
        return taskDTOs;
    }
    public Collection<TaskDTO> getUserTaskByStatus(long userID, String status) {
        User user = this.userRepository.findUserById(userID).orElseThrow(
                () -> new UserNotFoundException("User by id " + userID + " was not found"));
        Collection<Task> tasks = user.getTasks();
        LocalDate today = LocalDate.now();

        Collection<TaskDTO> taskDTOs = switch (status) {
            case "present" -> tasks.stream()
                    .filter(task -> !task.getStartingDate().isAfter(today) &&
                            !task.getEndingDate().isBefore(today))
                    .map(taskDTOMapper)
                    .collect(Collectors.toList());
            case "past" -> tasks.stream()
                    .filter(task -> task.getEndingDate().isBefore(today))
                    .map(taskDTOMapper)
                    .collect(Collectors.toList());
            case "future" -> tasks.stream()
                    .filter(task -> task.getStartingDate().isAfter(today))
                    .map(taskDTOMapper)
                    .collect(Collectors.toList());
            default -> tasks.stream()
                    .map(taskDTOMapper)
                    .collect(Collectors.toList());
        };
        return taskDTOs;
    }
    public Task editTask(Task task) {
        java.util.Date date = new java.util.Date();
        Task taskToUpdate = taskRepository.findById(task.getId());

        if (taskToUpdate != null) {
            // Aktualizuj właściwości obiektu taskToUpdate na podstawie obiektu task
            taskToUpdate.setTopic(task.getTopic());
            taskToUpdate.setDescription(task.getDescription());
            taskToUpdate.setHours(task.getHours());
            taskToUpdate.setStartingDate(task.getStartingDate());
            taskToUpdate.setEndingDate(task.getEndingDate());

            taskToUpdate = taskRepository.save(taskToUpdate); // Zapisz zaktualizowany obiekt do bazy danych
            System.out.println("[" + date + "]" + "[TASK]: " + taskToUpdate.getId() + " was updated.");
        } else {
            // Obsłuż przypadek, gdy obiekt taskToUpdate nie został znaleziony w bazie danych
            System.out.println("[" + date + "]" + "[TASK]: Task with ID " + task.getId() + " not found.");
        }

        return taskToUpdate;
    }
    public TaskDTO getTaskById(long id) {
        Task task = taskRepository.findById(id);
        return taskDTOMapper.apply(task);
    }

    public void createTask(String username, Task task) {
        User user = this.userRepository.findByName(username);
        if (user == null) {
            throw new UserNotFoundException("User with username " + username + " not found.");
        }
        taskRepository.save(task);
        user.addTask(task);
        userRepository.save(user);
    }
}