package com.example.teamflow.service.impl;

import com.example.teamflow.dto.ActivityDTO;
import com.example.teamflow.dto.DashboardResponseDTO;
import com.example.teamflow.entity.*;
import com.example.teamflow.repository.*;
import com.example.teamflow.security.SecurityUtils;
import com.example.teamflow.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ReviewRepository reviewRepository;
    private final SecurityUtils securityUtils;

    @Override
    public DashboardResponseDTO getDashboardData() {
        long totalProjects = projectRepository.count();
        long totalTasks = taskRepository.count();
        long completedTasks = taskRepository.countByStatus(TaskStatus.COMPLETED);
        long pendingTasks = taskRepository.countByStatus(TaskStatus.TODO);
        long inProgressTasks = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        long blockedTasks = taskRepository.countByStatus(TaskStatus.BLOCKED);
        long totalIncidents = incidentRepository.count();
        long openIncidents = incidentRepository.countByStatus(IncidentStatus.OPEN)
                + incidentRepository.countByStatus(IncidentStatus.IN_PROGRESS);
        long totalUsers = userRepository.count();

        long unreadNotifications = 0;
        User currentUser = securityUtils.getCurrentUser();
        if (currentUser != null) {
            unreadNotifications = notificationRepository.countByRecipientAndReadFalse(currentUser);
        }

        Map<String, Long> tasksByStatus = Arrays.stream(TaskStatus.values())
                .collect(Collectors.toMap(Enum::name, taskRepository::countByStatus));

        Map<String, Long> incidentsBySeverity = Arrays.stream(IncidentSeverity.values())
                .collect(Collectors.toMap(Enum::name,
                        s -> incidentRepository.findAll().stream()
                                .filter(i -> i.getSeverity() == s).count()));

        Map<String, Long> projectsByStatus = Arrays.stream(ProjectStatus.values())
                .collect(Collectors.toMap(Enum::name, projectRepository::countByStatus));

        List<ActivityDTO> recentActivities = buildRecentActivities();

        return DashboardResponseDTO.builder()
                .totalProjects(totalProjects)
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .pendingTasks(pendingTasks)
                .inProgressTasks(inProgressTasks)
                .blockedTasks(blockedTasks)
                .totalIncidents(totalIncidents)
                .openIncidents(openIncidents)
                .totalUsers(totalUsers)
                .unreadNotifications(unreadNotifications)
                .tasksByStatus(tasksByStatus)
                .incidentsBySeverity(incidentsBySeverity)
                .projectsByStatus(projectsByStatus)
                .recentActivities(recentActivities)
                .build();
    }

    private List<ActivityDTO> buildRecentActivities() {
        List<ActivityDTO> activities = new ArrayList<>();

        taskRepository.findAll().stream()
                .sorted(Comparator.comparing(Task::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .forEach(task -> activities.add(ActivityDTO.builder()
                        .type("TASK")
                        .description("Task created: " + task.getTitle())
                        .userName(task.getAssignedTo() != null ? task.getAssignedTo().getName() : "Unassigned")
                        .timestamp(task.getCreatedAt())
                        .build()));

        incidentRepository.findAll().stream()
                .sorted(Comparator.comparing(Incident::getCreatedDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(3)
                .forEach(incident -> activities.add(ActivityDTO.builder()
                        .type("INCIDENT")
                        .description("Incident reported: " + incident.getTitle())
                        .userName(incident.getReportedBy().getName())
                        .timestamp(incident.getCreatedDate())
                        .build()));

        reviewRepository.findAll().stream()
                .sorted(Comparator.comparing(Review::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(3)
                .forEach(review -> activities.add(ActivityDTO.builder()
                        .type("REVIEW")
                        .description("Review " + review.getStatus().name().toLowerCase() + ": " + review.getTask().getTitle())
                        .userName(review.getReviewer().getName())
                        .timestamp(review.getCreatedAt())
                        .build()));

        return activities.stream()
                .sorted(Comparator.comparing(ActivityDTO::getTimestamp, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(10)
                .toList();
    }
}
