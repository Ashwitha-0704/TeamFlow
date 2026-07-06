package com.example.teamflow.service;

import com.example.teamflow.dto.IncidentRequestDTO;
import com.example.teamflow.dto.IncidentResponseDTO;

import java.util.List;

public interface IncidentService {

    IncidentResponseDTO createIncident(IncidentRequestDTO request);

    IncidentResponseDTO updateIncident(Long id, IncidentRequestDTO request);

    IncidentResponseDTO getIncidentById(Long id);

    List<IncidentResponseDTO> getAllIncidents();

    void deleteIncident(Long id);
}
