package com.example.teamflow.repository;

import com.example.teamflow.entity.Review;
import com.example.teamflow.entity.ReviewStatus;
import com.example.teamflow.entity.Task;
import com.example.teamflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByReviewer(User reviewer);

    List<Review> findByTask(Task task);

    List<Review> findByStatus(ReviewStatus status);

    Optional<Review> findTopByTaskOrderByCreatedAtDesc(Task task);

    long countByStatus(ReviewStatus status);
}
