package com.example.teamflow.dto;

import com.example.teamflow.entity.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {

    private Long id;
    private Long taskId;
    private String taskTitle;
    private Long reviewerId;
    private String reviewerName;
    private Long assignedToId;
    private String assignedToName;
    private String comments;
    private ReviewStatus status;
    private LocalDateTime approvedDate;
    private LocalDateTime createdAt;
}
