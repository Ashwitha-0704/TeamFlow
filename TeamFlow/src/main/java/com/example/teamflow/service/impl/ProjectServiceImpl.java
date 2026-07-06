package com.example.teamflow.service.impl;

import com.example.teamflow.dto.ProjectRequestDTO;
import com.example.teamflow.dto.ProjectResponseDTO;
import com.example.teamflow.entity.Project;
import com.example.teamflow.entity.ProjectStatus;
import com.example.teamflow.entity.User;
import com.example.teamflow.exception.ResourceNotFoundException;
import com.example.teamflow.repository.ProjectRepository;
import com.example.teamflow.repository.UserRepository;
import com.example.teamflow.service.ProjectService;
import com.example.teamflow.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;

    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO request) {
        User manager = findUserById(request.getManagerId());

        Project project = Project.builder()
                .projectName(request.getProjectName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus() != null ? request.getStatus() : ProjectStatus.PLANNING)
                .manager(manager)
                .build();

        return entityMapper.toProjectResponseDTO(projectRepository.save(project));
    }

    @Override
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request) {
        Project project = findProjectById(id);
        User manager = findUserById(request.getManagerId());

        project.setProjectName(request.getProjectName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }
        project.setManager(manager);

        return entityMapper.toProjectResponseDTO(projectRepository.save(project));
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDTO getProjectById(Long id) {
        return entityMapper.toProjectResponseDTO(findProjectById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(entityMapper::toProjectResponseDTO)
                .toList();
    }

    @Override
    public void deleteProject(Long id) {
        Project project = findProjectById(id);
        projectRepository.delete(project);
    }

    private Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
