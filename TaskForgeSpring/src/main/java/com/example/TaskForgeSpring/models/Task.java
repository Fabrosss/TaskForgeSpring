package com.example.TaskForgeSpring.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
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
    @Column(name = "TASK_HOURS")
    private long hours;
    @Column(name = "TASK_STARTING_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startingDate;
    @Column(name = "TASK_ENDING_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endingDate;

    @JsonIgnore
    @ManyToMany(mappedBy = "tasks")
    private Collection<User> users;
}
