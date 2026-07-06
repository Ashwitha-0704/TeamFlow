package com.example.teamflow.dto;

import com.example.teamflow.entity.TaskPriority;
import com.example.teamflow.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private TaskPriority priority;

    private TaskStatus status;

    private Long assignedToId;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private Double estimatedHours;

    private Double actualHours;

    private LocalDate dueDate;

    private Long dependencyTaskId;
}
