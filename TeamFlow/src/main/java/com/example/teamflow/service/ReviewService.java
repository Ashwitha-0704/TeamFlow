package com.example.teamflow.service;

import com.example.teamflow.entity.Task;
import com.example.teamflow.dto.ReviewRequestDTO;
import com.example.teamflow.dto.ReviewResponseDTO;

import java.util.List;

public interface ReviewService {

    ReviewResponseDTO createReview(ReviewRequestDTO request);

    ReviewResponseDTO updateReview(Long id, ReviewRequestDTO request);

    ReviewResponseDTO getReviewById(Long id);

    List<ReviewResponseDTO> getAllReviews();

    List<ReviewResponseDTO> getPendingReviews();

    void deleteReview(Long id);

    ReviewResponseDTO createReviewForCompletedTask(Task task);
}
