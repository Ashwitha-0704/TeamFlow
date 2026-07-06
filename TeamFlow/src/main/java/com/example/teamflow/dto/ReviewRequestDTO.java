package com.example.teamflow.dto;

import com.example.teamflow.entity.ReviewStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {

    @NotNull(message = "Task ID is required")
    private Long taskId;

    @NotNull(message = "Reviewer ID is required")
    private Long reviewerId;

    private String comments;

    private ReviewStatus status;
}
