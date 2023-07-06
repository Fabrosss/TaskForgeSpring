package com.example.TaskForgeSpring.service;

import com.example.TaskForgeSpring.models.Task;
import com.example.TaskForgeSpring.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(long taskId) {
        taskRepository.deleteTaskById(taskId);
    }



}
