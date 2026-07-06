package com.example.teamflow.controller;

import com.example.teamflow.dto.ReportsResponseDTO;
import com.example.teamflow.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ReportsResponseDTO> getReports() {
        return ResponseEntity.ok(reportService.generateReports());
    }
}
