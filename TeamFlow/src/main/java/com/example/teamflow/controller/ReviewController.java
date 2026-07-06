package com.example.teamflow.controller;

import com.example.teamflow.dto.ReviewRequestDTO;
import com.example.teamflow.dto.ReviewResponseDTO;
import com.example.teamflow.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long id,
                                                        @Valid @RequestBody ReviewRequestDTO request) {
        return ResponseEntity.ok(reviewService.updateReview(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ReviewResponseDTO>> getPendingReviews() {
        return ResponseEntity.ok(reviewService.getPendingReviews());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
