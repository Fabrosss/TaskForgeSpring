package com.example.TaskForgeSpring.mapper;

import com.example.TaskForgeSpring.model.DTO.TaskDTO;
import com.example.TaskForgeSpring.models.Task;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class TaskDTOMapper implements Function<Task, TaskDTO> {
    @Override
    public TaskDTO apply(Task task){
        return new TaskDTO(
                task.getTopic(),
                task.getDescription()
        );
    }
}
