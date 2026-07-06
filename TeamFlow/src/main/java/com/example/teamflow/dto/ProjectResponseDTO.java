package com.example.teamflow.dto;

import com.example.teamflow.entity.ProjectStatus;
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
public class ProjectResponseDTO {

    private Long id;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private Long managerId;
    private String managerName;
    private long taskCount;
    private long completedTaskCount;
    private LocalDateTime createdAt;
}
