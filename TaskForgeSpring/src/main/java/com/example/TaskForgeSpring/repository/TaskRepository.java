package com.example.TaskForgeSpring.repository;

import com.example.TaskForgeSpring.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    Task findById(long id);
    Task findByTopic(String topic);
    @Transactional
    void deleteTaskById(long id);


}
