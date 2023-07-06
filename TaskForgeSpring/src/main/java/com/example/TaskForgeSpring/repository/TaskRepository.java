package com.example.TaskForgeSpring.repository;

import com.example.TaskForgeSpring.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    Task findById(long id);

    void deleteTaskById(long id);


}
