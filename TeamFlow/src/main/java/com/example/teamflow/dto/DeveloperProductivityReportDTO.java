package com.example.teamflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperProductivityReportDTO {

    private Long developerId;
    private String developerName;
    private long assignedTasks;
    private long completedTasks;
    private double totalEstimatedHours;
    private double totalActualHours;
    private double productivityScore;
}
