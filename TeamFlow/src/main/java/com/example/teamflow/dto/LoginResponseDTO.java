package com.example.teamflow.dto;

import com.example.teamflow.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private String message;
}
