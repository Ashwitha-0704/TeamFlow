package com.example.teamflow.service.impl;

import com.example.teamflow.dto.IncidentRequestDTO;
import com.example.teamflow.dto.IncidentResponseDTO;
import com.example.teamflow.entity.*;
import com.example.teamflow.exception.ResourceNotFoundException;
import com.example.teamflow.repository.IncidentRepository;
import com.example.teamflow.repository.ProjectRepository;
import com.example.teamflow.repository.UserRepository;
import com.example.teamflow.security.SecurityUtils;
import com.example.teamflow.service.IncidentService;
import com.example.teamflow.service.NotificationService;
import com.example.teamflow.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;
    private final NotificationService notificationService;
    private final SecurityUtils securityUtils;

    @Override
    public IncidentResponseDTO createIncident(IncidentRequestDTO request) {
        Project project = findProjectById(request.getProjectId());
        User reporter = securityUtils.getCurrentUser();
        if (reporter == null) {
            throw new ResourceNotFoundException("Authenticated user not found");
        }

        Incident incident = Incident.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .severity(request.getSeverity() != null ? request.getSeverity() : IncidentSeverity.MEDIUM)
                .reportedBy(reporter)
                .project(project)
                .status(IncidentStatus.OPEN)
                .build();

        if (request.getAssignedEngineerId() != null) {
            incident.setAssignedEngineer(findUserById(request.getAssignedEngineerId()));
        }

        Incident saved = incidentRepository.save(incident);

        if (project.getManager() != null) {
            notificationService.createNotification(
                    project.getManager(),
                    "Incident Created",
                    "New incident reported: " + saved.getTitle(),
                    NotificationType.INCIDENT_CREATED,
                    saved.getId()
            );
        }

        if (saved.getAssignedEngineer() != null) {
            notificationService.createNotification(
                    saved.getAssignedEngineer(),
                    "Incident Assigned",
                    "You have been assigned incident: " + saved.getTitle(),
                    NotificationType.INCIDENT_CREATED,
                    saved.getId()
            );
        }

        return entityMapper.toIncidentResponseDTO(saved);
    }

    @Override
    public IncidentResponseDTO updateIncident(Long id, IncidentRequestDTO request) {
        Incident incident = findIncidentById(id);
        Project project = findProjectById(request.getProjectId());

        incident.setTitle(request.getTitle());
        incident.setDescription(request.getDescription());
        if (request.getSeverity() != null) {
            incident.setSeverity(request.getSeverity());
        }
        incident.setProject(project);

        if (request.getAssignedEngineerId() != null) {
            incident.setAssignedEngineer(findUserById(request.getAssignedEngineerId()));
        }

        if (request.getStatus() != null) {
            incident.setStatus(request.getStatus());
            if (request.getStatus() == IncidentStatus.RESOLVED || request.getStatus() == IncidentStatus.CLOSED) {
                incident.setResolvedDate(LocalDateTime.now());
            }
        }

        return entityMapper.toIncidentResponseDTO(incidentRepository.save(incident));
    }

    @Override
    @Transactional(readOnly = true)
    public IncidentResponseDTO getIncidentById(Long id) {
        return entityMapper.toIncidentResponseDTO(findIncidentById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentResponseDTO> getAllIncidents() {
        return incidentRepository.findAll().stream()
                .map(entityMapper::toIncidentResponseDTO)
                .toList();
    }

    @Override
    public void deleteIncident(Long id) {
        Incident incident = findIncidentById(id);
        incidentRepository.delete(incident);
    }

    private Incident findIncidentById(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found with id: " + id));
    }

    private Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
