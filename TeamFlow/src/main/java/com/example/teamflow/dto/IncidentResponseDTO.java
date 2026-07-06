package com.example.teamflow.dto;

import com.example.teamflow.entity.IncidentSeverity;
import com.example.teamflow.entity.IncidentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentResponseDTO {

    private Long id;
    private String title;
    private String description;
    private IncidentSeverity severity;
    private Long reportedById;
    private String reportedByName;
    private Long assignedEngineerId;
    private String assignedEngineerName;
    private IncidentStatus status;
    private Long projectId;
    private String projectName;
    private LocalDateTime createdDate;
    private LocalDateTime resolvedDate;
}
