package com.example.teamflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCompletionReportDTO {

    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private double completionRate;
    private Map<String, Long> tasksByPriority;
}
