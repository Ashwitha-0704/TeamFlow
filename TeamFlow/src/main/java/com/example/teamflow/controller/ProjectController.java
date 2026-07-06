package com.example.teamflow.controller;

import com.example.teamflow.dto.ProjectRequestDTO;
import com.example.teamflow.dto.ProjectResponseDTO;
import com.example.teamflow.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long id,
                                                            @Valid @RequestBody ProjectRequestDTO request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
