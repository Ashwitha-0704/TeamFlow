package com.example.teamflow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Project> managedProjects = new ArrayList<>();

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Task> assignedTasks = new ArrayList<>();

    @OneToMany(mappedBy = "reportedBy", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Incident> reportedIncidents = new ArrayList<>();

    @OneToMany(mappedBy = "assignedEngineer", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Incident> assignedIncidents = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();
}
