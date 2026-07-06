package com.example.teamflow.controller;

import com.example.teamflow.dto.IncidentRequestDTO;
import com.example.teamflow.dto.IncidentResponseDTO;
import com.example.teamflow.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping
    public ResponseEntity<IncidentResponseDTO> createIncident(@Valid @RequestBody IncidentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incidentService.createIncident(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> updateIncident(@PathVariable Long id,
                                                              @Valid @RequestBody IncidentRequestDTO request) {
        return ResponseEntity.ok(incidentService.updateIncident(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> getIncidentById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getIncidentById(id));
    }

    @GetMapping
    public ResponseEntity<List<IncidentResponseDTO>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }
}
