package com.example.teamflow.service;

import com.example.teamflow.dto.NotificationResponseDTO;
import com.example.teamflow.entity.NotificationType;
import com.example.teamflow.entity.User;

import java.util.List;

public interface NotificationService {

    NotificationResponseDTO createNotification(User recipient, String title, String message,
                                               NotificationType type, Long referenceId);

    List<NotificationResponseDTO> getNotificationsForCurrentUser();

    List<NotificationResponseDTO> getUnreadNotificationsForCurrentUser();

    NotificationResponseDTO markAsRead(Long id);

    void markAllAsRead();

    long getUnreadCount();
}
