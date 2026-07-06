package com.example.teamflow.service;

import com.example.teamflow.dto.LoginRequestDTO;
import com.example.teamflow.dto.LoginResponseDTO;
import com.example.teamflow.dto.UserRequestDTO;
import com.example.teamflow.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO request);

    UserResponseDTO updateUser(Long id, UserRequestDTO request);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    void deleteUser(Long id);

    LoginResponseDTO login(LoginRequestDTO request);

    UserResponseDTO getCurrentUserProfile();
}
