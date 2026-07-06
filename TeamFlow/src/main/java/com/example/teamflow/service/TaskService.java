package com.example.teamflow.service;

import com.example.teamflow.dto.TaskRequestDTO;
import com.example.teamflow.dto.TaskResponseDTO;

import java.util.List;

public interface TaskService {

    TaskResponseDTO createTask(TaskRequestDTO request);

    TaskResponseDTO updateTask(Long id, TaskRequestDTO request);

    TaskResponseDTO getTaskById(Long id);

    List<TaskResponseDTO> getAllTasks();

    List<TaskResponseDTO> getTasksByProject(Long projectId);

    void deleteTask(Long id);
}
