package com.example.teamflow.controller;

import com.example.teamflow.dto.TaskRequestDTO;
import com.example.teamflow.dto.TaskResponseDTO;
import com.example.teamflow.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id,
                                                      @Valid @RequestBody TaskRequestDTO request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
