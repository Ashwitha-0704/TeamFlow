package com.example.teamflow.service.impl;

import com.example.teamflow.dto.TaskRequestDTO;
import com.example.teamflow.dto.TaskResponseDTO;
import com.example.teamflow.entity.*;
import com.example.teamflow.exception.BusinessException;
import com.example.teamflow.exception.ResourceNotFoundException;
import com.example.teamflow.repository.ProjectRepository;
import com.example.teamflow.repository.TaskRepository;
import com.example.teamflow.repository.UserRepository;
import com.example.teamflow.service.NotificationService;
import com.example.teamflow.service.ReviewService;
import com.example.teamflow.service.TaskService;
import com.example.teamflow.util.EntityMapper;
import com.example.teamflow.util.TaskStatusValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;
    private final NotificationService notificationService;
    @Lazy
    private final ReviewService reviewService;

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO request) {
        Project project = findProjectById(request.getProjectId());
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM)
                .status(TaskStatus.TODO)
                .project(project)
                .estimatedHours(request.getEstimatedHours())
                .actualHours(request.getActualHours())
                .dueDate(request.getDueDate())
                .build();

        if (request.getAssignedToId() != null) {
            User assignee = findUserById(request.getAssignedToId());
            task.setAssignedTo(assignee);
        }

        if (request.getDependencyTaskId() != null) {
            Task dependency = findTaskById(request.getDependencyTaskId());
            validateDependency(project, dependency, null);
            task.setDependencyTask(dependency);
        }

        Task saved = taskRepository.save(task);

        if (saved.getAssignedTo() != null) {
            notificationService.createNotification(
                    saved.getAssignedTo(),
                    "Task Assigned",
                    "You have been assigned task: " + saved.getTitle(),
                    NotificationType.TASK_ASSIGNED,
                    saved.getId()
            );
        }

        return entityMapper.toTaskResponseDTO(saved);
    }

    @Override
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO request) {
        Task task = findTaskById(id);
        TaskStatus previousStatus = task.getStatus();
        Project project = findProjectById(request.getProjectId());

        if (request.getStatus() != null && !TaskStatusValidator.isValidTransition(task.getStatus(), request.getStatus())) {
            throw new BusinessException("Invalid status transition from " + task.getStatus() + " to " + request.getStatus());
        }

        if (request.getStatus() == TaskStatus.IN_PROGRESS && task.getDependencyTask() != null) {
            if (!TaskStatusValidator.canStart(task.getDependencyTask().getStatus())) {
                throw new BusinessException("Cannot start task until dependency '" +
                        task.getDependencyTask().getTitle() + "' is completed");
            }
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        task.setProject(project);
        task.setEstimatedHours(request.getEstimatedHours());
        task.setActualHours(request.getActualHours());
        task.setDueDate(request.getDueDate());

        User previousAssignee = task.getAssignedTo();

        if (request.getAssignedToId() != null) {
            task.setAssignedTo(findUserById(request.getAssignedToId()));
        } else {
            task.setAssignedTo(null);
        }

        if (request.getDependencyTaskId() != null) {
            Task dependency = findTaskById(request.getDependencyTaskId());
            validateDependency(project, dependency, id);
            task.setDependencyTask(dependency);
        } else {
            task.setDependencyTask(null);
        }

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        Task saved = taskRepository.save(task);

        if (saved.getAssignedTo() != null && (previousAssignee == null
                || !previousAssignee.getId().equals(saved.getAssignedTo().getId()))) {
            notificationService.createNotification(
                    saved.getAssignedTo(),
                    "Task Assigned",
                    "You have been assigned task: " + saved.getTitle(),
                    NotificationType.TASK_ASSIGNED,
                    saved.getId()
            );
        }

        if (request.getStatus() == TaskStatus.IN_REVIEW && previousStatus != TaskStatus.IN_REVIEW) {
            reviewService.createReviewForCompletedTask(saved);
        }

        if (request.getStatus() == TaskStatus.COMPLETED && previousStatus != TaskStatus.COMPLETED) {
            if (saved.getAssignedTo() != null) {
                notificationService.createNotification(
                        saved.getAssignedTo(),
                        "Task Completed",
                        "Task completed: " + saved.getTitle(),
                        NotificationType.TASK_COMPLETED,
                        saved.getId()
                );
            }
        }

        return entityMapper.toTaskResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(Long id) {
        return entityMapper.toTaskResponseDTO(findTaskById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(entityMapper::toTaskResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getTasksByProject(Long projectId) {
        Project project = findProjectById(projectId);
        return taskRepository.findByProject(project).stream()
                .map(entityMapper::toTaskResponseDTO)
                .toList();
    }

    @Override
    public void deleteTask(Long id) {
        Task task = findTaskById(id);
        taskRepository.delete(task);
    }

    private void validateDependency(Project project, Task dependency, Long currentTaskId) {
        if (currentTaskId != null && dependency.getId().equals(currentTaskId)) {
            throw new BusinessException("Task cannot depend on itself");
        }
        if (!dependency.getProject().getId().equals(project.getId())) {
            throw new BusinessException("Dependency task must belong to the same project");
        }
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
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
