package com.example.teamflow.dto;

import com.example.teamflow.entity.ProjectStatus;
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
public class ProjectRequestDTO {

    @NotBlank(message = "Project name is required")
    private String projectName;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private ProjectStatus status;

    @NotNull(message = "Manager ID is required")
    private Long managerId;
}
