package com.example.teamflow.repository;

import com.example.teamflow.entity.Project;
import com.example.teamflow.entity.ProjectStatus;
import com.example.teamflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByManager(User manager);

    List<Project> findByStatus(ProjectStatus status);

    long countByStatus(ProjectStatus status);
}
