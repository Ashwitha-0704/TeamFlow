package com.example.teamflow.dto;

import com.example.teamflow.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private boolean read;
    private Long referenceId;
    private LocalDateTime createdAt;
}
