package com.example.teamflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {

    private String type;
    private String description;
    private String userName;
    private LocalDateTime timestamp;
}
