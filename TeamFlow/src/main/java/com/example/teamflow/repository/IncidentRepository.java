package com.example.teamflow.repository;

import com.example.teamflow.entity.Incident;
import com.example.teamflow.entity.IncidentStatus;
import com.example.teamflow.entity.Project;
import com.example.teamflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByProject(Project project);

    List<Incident> findByReportedBy(User user);

    List<Incident> findByAssignedEngineer(User user);

    List<Incident> findByStatus(IncidentStatus status);

    long countByStatus(IncidentStatus status);
}
