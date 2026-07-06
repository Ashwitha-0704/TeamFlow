package com.example.teamflow.util;

import com.example.teamflow.dto.*;
import com.example.teamflow.entity.*;
import com.example.teamflow.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityMapper {

    private final TaskRepository taskRepository;

    public UserResponseDTO toUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public ProjectResponseDTO toProjectResponseDTO(Project project) {
        long taskCount = taskRepository.countByProject(project);
        long completedCount = taskRepository.countByProjectAndStatus(project, TaskStatus.COMPLETED);
        return ProjectResponseDTO.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .status(project.getStatus())
                .managerId(project.getManager().getId())
                .managerName(project.getManager().getName())
                .taskCount(taskCount)
                .completedTaskCount(completedCount)
                .createdAt(project.getCreatedAt())
                .build();
    }

    public TaskResponseDTO toTaskResponseDTO(Task task) {
        TaskResponseDTO.TaskResponseDTOBuilder builder = TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .projectId(task.getProject().getId())
                .projectName(task.getProject().getProjectName())
                .estimatedHours(task.getEstimatedHours())
                .actualHours(task.getActualHours())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt());

        if (task.getAssignedTo() != null) {
            builder.assignedToId(task.getAssignedTo().getId())
                    .assignedToName(task.getAssignedTo().getName());
        }
        if (task.getDependencyTask() != null) {
            builder.dependencyTaskId(task.getDependencyTask().getId())
                    .dependencyTaskTitle(task.getDependencyTask().getTitle());
        }
        return builder.build();
    }

    public IncidentResponseDTO toIncidentResponseDTO(Incident incident) {
        IncidentResponseDTO.IncidentResponseDTOBuilder builder = IncidentResponseDTO.builder()
                .id(incident.getId())
                .title(incident.getTitle())
                .description(incident.getDescription())
                .severity(incident.getSeverity())
                .reportedById(incident.getReportedBy().getId())
                .reportedByName(incident.getReportedBy().getName())
                .status(incident.getStatus())
                .projectId(incident.getProject().getId())
                .projectName(incident.getProject().getProjectName())
                .createdDate(incident.getCreatedDate())
                .resolvedDate(incident.getResolvedDate());

        if (incident.getAssignedEngineer() != null) {
            builder.assignedEngineerId(incident.getAssignedEngineer().getId())
                    .assignedEngineerName(incident.getAssignedEngineer().getName());
        }
        return builder.build();
    }

    public ReviewResponseDTO toReviewResponseDTO(Review review) {
        ReviewResponseDTO.ReviewResponseDTOBuilder builder = ReviewResponseDTO.builder()
                .id(review.getId())
                .taskId(review.getTask().getId())
                .taskTitle(review.getTask().getTitle())
                .reviewerId(review.getReviewer().getId())
                .reviewerName(review.getReviewer().getName())
                .comments(review.getComments())
                .status(review.getStatus())
                .approvedDate(review.getApprovedDate())
                .createdAt(review.getCreatedAt());

        if (review.getTask().getAssignedTo() != null) {
            builder.assignedToId(review.getTask().getAssignedTo().getId())
                    .assignedToName(review.getTask().getAssignedTo().getName());
        }
        return builder.build();
    }

    public NotificationResponseDTO toNotificationResponseDTO(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .read(notification.isRead())
                .referenceId(notification.getReferenceId())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
