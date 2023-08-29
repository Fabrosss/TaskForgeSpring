package com.example.TaskForgeSpring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Collection;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "ROLES_TABLE")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ROLE_ID", nullable = false, unique = true)
    private long id;
    @Column
    private String name;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

}
