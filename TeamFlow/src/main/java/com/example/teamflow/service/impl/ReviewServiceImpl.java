package com.example.teamflow.service.impl;

import com.example.teamflow.dto.ReviewRequestDTO;
import com.example.teamflow.dto.ReviewResponseDTO;
import com.example.teamflow.entity.*;
import com.example.teamflow.exception.BusinessException;
import com.example.teamflow.exception.ResourceNotFoundException;
import com.example.teamflow.repository.ReviewRepository;
import com.example.teamflow.repository.TaskRepository;
import com.example.teamflow.repository.UserRepository;
import com.example.teamflow.service.NotificationService;
import com.example.teamflow.service.ReviewService;
import com.example.teamflow.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;
    private final NotificationService notificationService;

    @Override
    public ReviewResponseDTO createReview(ReviewRequestDTO request) {
        Task task = findTaskById(request.getTaskId());
        User reviewer = findUserById(request.getReviewerId());

        validateReviewerNotAssignee(task, reviewer);

        Review review = Review.builder()
                .task(task)
                .reviewer(reviewer)
                .comments(request.getComments())
                .status(ReviewStatus.PENDING)
                .build();

        return entityMapper.toReviewResponseDTO(reviewRepository.save(review));
    }

    /**
     * Auto-create review when task moves to IN_REVIEW status.
     */
    @Override
    public ReviewResponseDTO createReviewForCompletedTask(Task task) {
        User reviewer = userRepository.findByRole(Role.REVIEWER).stream()
                .findFirst()
                .orElse(null);

        if (reviewer == null) {
            reviewer = task.getProject().getManager();
        }

        if (reviewer == null) {
            throw new BusinessException("No reviewer available for task review");
        }

        validateReviewerNotAssignee(task, reviewer);

        Review review = Review.builder()
                .task(task)
                .reviewer(reviewer)
                .status(ReviewStatus.PENDING)
                .build();

        Review saved = reviewRepository.save(review);

        notificationService.createNotification(
                reviewer,
                "Review Required",
                "Please review task: " + task.getTitle(),
                NotificationType.TASK_COMPLETED,
                task.getId()
        );

        return entityMapper.toReviewResponseDTO(saved);
    }

    @Override
    public ReviewResponseDTO updateReview(Long id, ReviewRequestDTO request) {
        Review review = findReviewById(id);
        Task task = review.getTask();

        if (request.getComments() != null) {
            review.setComments(request.getComments());
        }

        if (request.getStatus() != null) {
            review.setStatus(request.getStatus());

            if (request.getStatus() == ReviewStatus.APPROVED) {
                review.setApprovedDate(LocalDateTime.now());
                task.setStatus(TaskStatus.COMPLETED);
                taskRepository.save(task);

                if (task.getAssignedTo() != null) {
                    notificationService.createNotification(
                            task.getAssignedTo(),
                            "Review Approved",
                            "Your task '" + task.getTitle() + "' has been approved",
                            NotificationType.REVIEW_APPROVED,
                            review.getId()
                    );
                }
            } else if (request.getStatus() == ReviewStatus.REJECTED) {
                task.setStatus(TaskStatus.IN_PROGRESS);
                taskRepository.save(task);

                if (task.getAssignedTo() != null) {
                    notificationService.createNotification(
                            task.getAssignedTo(),
                            "Review Rejected",
                            "Your task '" + task.getTitle() + "' was rejected. Please revise.",
                            NotificationType.REVIEW_REJECTED,
                            review.getId()
                    );
                }
            }
        }

        return entityMapper.toReviewResponseDTO(reviewRepository.save(review));
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDTO getReviewById(Long id) {
        return entityMapper.toReviewResponseDTO(findReviewById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(entityMapper::toReviewResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getPendingReviews() {
        return reviewRepository.findByStatus(ReviewStatus.PENDING).stream()
                .map(entityMapper::toReviewResponseDTO)
                .toList();
    }

    @Override
    public void deleteReview(Long id) {
        Review review = findReviewById(id);
        reviewRepository.delete(review);
    }

    private void validateReviewerNotAssignee(Task task, User reviewer) {
        if (task.getAssignedTo() != null && task.getAssignedTo().getId().equals(reviewer.getId())) {
            throw new BusinessException("Reviewer cannot review their own task");
        }
    }

    private Review findReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
