package com.example.teamflow.config;

import com.example.teamflow.entity.*;
import com.example.teamflow.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class SampleDataInitializer implements CommandLineRunner {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (projectRepository.count() > 0) {
            return;
        }

        log.info("Initializing sample data...");

        User manager = userRepository.findByEmail("manager@teamflow.com").orElseThrow();
        User developer = userRepository.findByEmail("developer@teamflow.com").orElseThrow();

        Project project1 = projectRepository.save(Project.builder()
                .projectName("TeamFlow Platform")
                .description("Core project management platform development")
                .startDate(LocalDate.now().minusDays(30))
                .endDate(LocalDate.now().plusDays(60))
                .status(ProjectStatus.ACTIVE)
                .manager(manager)
                .build());

        Project project2 = projectRepository.save(Project.builder()
                .projectName("Mobile App Integration")
                .description("Integrate mobile app with backend APIs")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(90))
                .status(ProjectStatus.PLANNING)
                .manager(manager)
                .build());

        Task task1 = taskRepository.save(Task.builder()
                .title("Setup Database Schema")
                .description("Design and implement MySQL schema")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.COMPLETED)
                .assignedTo(developer)
                .project(project1)
                .estimatedHours(8.0)
                .actualHours(6.0)
                .dueDate(LocalDate.now().minusDays(5))
                .build());

        Task task2 = taskRepository.save(Task.builder()
                .title("Implement User Authentication")
                .description("Spring Security with role-based access")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.IN_PROGRESS)
                .assignedTo(developer)
                .project(project1)
                .estimatedHours(16.0)
                .actualHours(10.0)
                .dueDate(LocalDate.now().plusDays(7))
                .dependencyTask(task1)
                .build());

        taskRepository.save(Task.builder()
                .title("Build REST APIs")
                .description("Create CRUD APIs for all modules")
                .priority(TaskPriority.MEDIUM)
                .status(TaskStatus.TODO)
                .assignedTo(developer)
                .project(project1)
                .estimatedHours(24.0)
                .dueDate(LocalDate.now().plusDays(14))
                .dependencyTask(task2)
                .build());

        taskRepository.save(Task.builder()
                .title("Design UI Mockups")
                .description("Create Bootstrap-based responsive UI")
                .priority(TaskPriority.LOW)
                .status(TaskStatus.TODO)
                .assignedTo(developer)
                .project(project2)
                .estimatedHours(12.0)
                .dueDate(LocalDate.now().plusDays(20))
                .build());

        incidentRepository.save(Incident.builder()
                .title("Login page slow on mobile")
                .description("Page load exceeds 3 seconds on mobile devices")
                .severity(IncidentSeverity.MEDIUM)
                .reportedBy(developer)
                .assignedEngineer(developer)
                .project(project1)
                .status(IncidentStatus.OPEN)
                .build());

        log.info("Sample data initialized successfully");
    }
}
