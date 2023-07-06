package com.example.TaskForgeSpring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Accessors(chain = true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "TASK_ID", nullable = false, unique = true)
    private long id;
    @Column(name = "TASK_TOPIC")
    private String topic;
    @Column(name = "TASK_DESCRIPTION")
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "tasks")
    private Collection<User> users;
}
