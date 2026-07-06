package com.example.teamflow.dto;

import com.example.teamflow.entity.IncidentSeverity;
import com.example.teamflow.entity.IncidentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private IncidentSeverity severity;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private Long assignedEngineerId;

    private IncidentStatus status;
}
