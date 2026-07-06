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
public class IncidentStatisticsReportDTO {

    private long totalIncidents;
    private long openIncidents;
    private long resolvedIncidents;
    private Map<String, Long> incidentsBySeverity;
    private Map<String, Long> incidentsByStatus;
}
