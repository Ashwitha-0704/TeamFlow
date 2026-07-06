package com.example.teamflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportsResponseDTO {

    private List<ProjectProgressReportDTO> projectProgress;
    private TaskCompletionReportDTO taskCompletion;
    private IncidentStatisticsReportDTO incidentStatistics;
    private List<DeveloperProductivityReportDTO> developerProductivity;
}
