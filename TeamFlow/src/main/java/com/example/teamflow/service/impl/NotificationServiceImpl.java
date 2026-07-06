package com.example.teamflow.service.impl;

import com.example.teamflow.dto.NotificationResponseDTO;
import com.example.teamflow.entity.Notification;
import com.example.teamflow.entity.NotificationType;
import com.example.teamflow.entity.User;
import com.example.teamflow.exception.ResourceNotFoundException;
import com.example.teamflow.repository.NotificationRepository;
import com.example.teamflow.security.SecurityUtils;
import com.example.teamflow.service.NotificationService;
import com.example.teamflow.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EntityMapper entityMapper;
    private final SecurityUtils securityUtils;

    @Override
    public NotificationResponseDTO createNotification(User recipient, String title, String message,
                                                        NotificationType type, Long referenceId) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .title(title)
                .message(message)
                .type(type)
                .referenceId(referenceId)
                .read(false)
                .build();

        return entityMapper.toNotificationResponseDTO(notificationRepository.save(notification));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getNotificationsForCurrentUser() {
        User user = getCurrentUser();
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(user).stream()
                .map(entityMapper::toNotificationResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getUnreadNotificationsForCurrentUser() {
        User user = getCurrentUser();
        return notificationRepository.findByRecipientAndReadFalseOrderByCreatedAtDesc(user).stream()
                .map(entityMapper::toNotificationResponseDTO)
                .toList();
    }

    @Override
    public NotificationResponseDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        notification.setRead(true);
        return entityMapper.toNotificationResponseDTO(notificationRepository.save(notification));
    }

    @Override
    public void markAllAsRead() {
        User user = getCurrentUser();
        notificationRepository.findByRecipientAndReadFalseOrderByCreatedAtDesc(user)
                .forEach(n -> n.setRead(true));
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount() {
        User user = getCurrentUser();
        return notificationRepository.countByRecipientAndReadFalse(user);
    }

    private User getCurrentUser() {
        User user = securityUtils.getCurrentUser();
        if (user == null) {
            throw new ResourceNotFoundException("Authenticated user not found");
        }
        return user;
    }
}
