package com.example.teamflow.dto;

import com.example.teamflow.entity.TaskPriority;
import com.example.teamflow.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private Long assignedToId;
    private String assignedToName;
    private Long projectId;
    private String projectName;
    private Double estimatedHours;
    private Double actualHours;
    private LocalDate dueDate;
    private Long dependencyTaskId;
    private String dependencyTaskTitle;
    private LocalDateTime createdAt;
}
