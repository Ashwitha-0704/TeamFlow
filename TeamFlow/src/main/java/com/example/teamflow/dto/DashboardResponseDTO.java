package com.example.teamflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {

    private long totalProjects;
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private long inProgressTasks;
    private long blockedTasks;
    private long totalIncidents;
    private long openIncidents;
    private long totalUsers;
    private long unreadNotifications;
    private Map<String, Long> tasksByStatus;
    private Map<String, Long> incidentsBySeverity;
    private Map<String, Long> projectsByStatus;
    private List<ActivityDTO> recentActivities;
}
