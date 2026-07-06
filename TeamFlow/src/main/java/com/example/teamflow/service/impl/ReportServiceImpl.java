package com.example.teamflow.service.impl;

import com.example.teamflow.dto.*;
import com.example.teamflow.entity.*;
import com.example.teamflow.repository.*;
import com.example.teamflow.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    @Override
    public ReportsResponseDTO generateReports() {
        return ReportsResponseDTO.builder()
                .projectProgress(generateProjectProgress())
                .taskCompletion(generateTaskCompletion())
                .incidentStatistics(generateIncidentStatistics())
                .developerProductivity(generateDeveloperProductivity())
                .build();
    }

    private List<ProjectProgressReportDTO> generateProjectProgress() {
        return projectRepository.findAll().stream()
                .map(project -> {
                    long total = taskRepository.countByProject(project);
                    long completed = taskRepository.countByProjectAndStatus(project, TaskStatus.COMPLETED);
                    double percentage = total > 0 ? (completed * 100.0 / total) : 0;

                    Map<String, Long> tasksByStatus = Arrays.stream(TaskStatus.values())
                            .collect(Collectors.toMap(Enum::name,
                                    s -> taskRepository.countByProjectAndStatus(project, s)));

                    return ProjectProgressReportDTO.builder()
                            .projectId(project.getId())
                            .projectName(project.getProjectName())
                            .totalTasks(total)
                            .completedTasks(completed)
                            .completionPercentage(Math.round(percentage * 100.0) / 100.0)
                            .tasksByStatus(tasksByStatus)
                            .build();
                })
                .toList();
    }

    private TaskCompletionReportDTO generateTaskCompletion() {
        long total = taskRepository.count();
        long completed = taskRepository.countByStatus(TaskStatus.COMPLETED);
        long pending = taskRepository.countByStatus(TaskStatus.TODO);
        double rate = total > 0 ? (completed * 100.0 / total) : 0;

        Map<String, Long> byPriority = Arrays.stream(TaskPriority.values())
                .collect(Collectors.toMap(Enum::name,
                        p -> taskRepository.findAll().stream()
                                .filter(t -> t.getPriority() == p).count()));

        return TaskCompletionReportDTO.builder()
                .totalTasks(total)
                .completedTasks(completed)
                .pendingTasks(pending)
                .completionRate(Math.round(rate * 100.0) / 100.0)
                .tasksByPriority(byPriority)
                .build();
    }

    private IncidentStatisticsReportDTO generateIncidentStatistics() {
        long total = incidentRepository.count();
        long open = incidentRepository.countByStatus(IncidentStatus.OPEN);
        long resolved = incidentRepository.countByStatus(IncidentStatus.RESOLVED)
                + incidentRepository.countByStatus(IncidentStatus.CLOSED);

        Map<String, Long> bySeverity = Arrays.stream(IncidentSeverity.values())
                .collect(Collectors.toMap(Enum::name,
                        s -> incidentRepository.findAll().stream()
                                .filter(i -> i.getSeverity() == s).count()));

        Map<String, Long> byStatus = Arrays.stream(IncidentStatus.values())
                .collect(Collectors.toMap(Enum::name, incidentRepository::countByStatus));

        return IncidentStatisticsReportDTO.builder()
                .totalIncidents(total)
                .openIncidents(open)
                .resolvedIncidents(resolved)
                .incidentsBySeverity(bySeverity)
                .incidentsByStatus(byStatus)
                .build();
    }

    private List<DeveloperProductivityReportDTO> generateDeveloperProductivity() {
        return userRepository.findByRole(Role.DEVELOPER).stream()
                .map(dev -> {
                    List<Task> tasks = taskRepository.findByAssignedTo(dev);
                    long completed = tasks.stream()
                            .filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
                    double estimated = tasks.stream()
                            .mapToDouble(t -> t.getEstimatedHours() != null ? t.getEstimatedHours() : 0).sum();
                    double actual = tasks.stream()
                            .mapToDouble(t -> t.getActualHours() != null ? t.getActualHours() : 0).sum();
                    double score = estimated > 0 ? (completed * 100.0 / tasks.size()) : 0;

                    return DeveloperProductivityReportDTO.builder()
                            .developerId(dev.getId())
                            .developerName(dev.getName())
                            .assignedTasks(tasks.size())
                            .completedTasks(completed)
                            .totalEstimatedHours(estimated)
                            .totalActualHours(actual)
                            .productivityScore(Math.round(score * 100.0) / 100.0)
                            .build();
                })
                .toList();
    }
}
