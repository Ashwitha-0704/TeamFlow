package com.example.teamflow.controller;

import com.example.teamflow.dto.LoginRequestDTO;
import com.example.teamflow.dto.LoginResponseDTO;
import com.example.teamflow.dto.UserResponseDTO;
import com.example.teamflow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final org.springframework.security.authentication.AuthenticationManager authenticationManager;
    private final org.springframework.security.web.context.SecurityContextRepository securityContextRepository = 
            new org.springframework.security.web.context.HttpSessionSecurityContextRepository();

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request,
                                                  jakarta.servlet.http.HttpServletRequest httpRequest,
                                                  jakarta.servlet.http.HttpServletResponse httpResponse) {
        org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        org.springframework.security.core.context.SecurityContext context = org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        org.springframework.security.core.context.SecurityContextHolder.setContext(context);
        
        securityContextRepository.saveContext(context, httpRequest, httpResponse);

        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }
}
