package com.example.teamflow.service.impl;

import com.example.teamflow.dto.LoginRequestDTO;
import com.example.teamflow.dto.LoginResponseDTO;
import com.example.teamflow.dto.UserRequestDTO;
import com.example.teamflow.dto.UserResponseDTO;
import com.example.teamflow.entity.User;
import com.example.teamflow.exception.BusinessException;
import com.example.teamflow.exception.ResourceNotFoundException;
import com.example.teamflow.repository.UserRepository;
import com.example.teamflow.security.SecurityUtils;
import com.example.teamflow.service.UserService;
import com.example.teamflow.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper entityMapper;
    private final AuthenticationManager authenticationManager;
    private final SecurityUtils securityUtils;

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists: " + request.getEmail());
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new BusinessException("Password must be at least 6 characters");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .active(true)
                .build();

        return entityMapper.toUserResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO request) {
        User user = findUserById(id);

        userRepository.findByEmail(request.getEmail())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException("Email already exists: " + request.getEmail());
                });

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return entityMapper.toUserResponseDTO(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return entityMapper.toUserResponseDTO(findUserById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(entityMapper::toUserResponseDTO)
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        User user = findUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return LoginResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .message("Login successful")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getCurrentUserProfile() {
        User user = securityUtils.getCurrentUser();
        if (user == null) {
            throw new BusinessException("No authenticated user found");
        }
        return entityMapper.toUserResponseDTO(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
