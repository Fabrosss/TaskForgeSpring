package com.example.TaskForgeSpring.models;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "USER_TABLE")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "USER_ID", nullable = false, unique = true)
    private long id;
    @Column(name = "USER_NAME")
    private String name;
    @Column(name = "USER_SURNAME")
    private String surname;
    @Column(name = "USER_EMAIL")
    private String email;
    @Column(name = "USER_PASSWORD")
    private String password;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    @ManyToMany
    @JoinTable(
            name="users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "USER_ID"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "ROLE_ID"))
    private Collection<Role> roles;

    @ManyToMany
    @JoinTable(
            name="users_tasks",
            joinColumns = @JoinColumn(
                    name="user_id", referencedColumnName = "USER_ID"),
            inverseJoinColumns = @JoinColumn(
                    name="task_id", referencedColumnName = "TASK_ID"
            )
    )
    private Collection<Task> tasks;
}
