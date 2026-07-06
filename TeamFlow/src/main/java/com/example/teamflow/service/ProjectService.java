package com.example.teamflow.service;

import com.example.teamflow.dto.ProjectRequestDTO;
import com.example.teamflow.dto.ProjectResponseDTO;

import java.util.List;

public interface ProjectService {

    ProjectResponseDTO createProject(ProjectRequestDTO request);

    ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request);

    ProjectResponseDTO getProjectById(Long id);

    List<ProjectResponseDTO> getAllProjects();

    void deleteProject(Long id);
}
