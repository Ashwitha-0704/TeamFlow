package com.example.teamflow.repository;

import com.example.teamflow.entity.Project;
import com.example.teamflow.entity.Task;
import com.example.teamflow.entity.TaskStatus;
import com.example.teamflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject(Project project);

    List<Task> findByAssignedTo(User user);

    List<Task> findByStatus(TaskStatus status);

    long countByStatus(TaskStatus status);

    long countByProject(Project project);

    long countByProjectAndStatus(Project project, TaskStatus status);
}
