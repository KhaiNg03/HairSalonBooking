package com.admin.service;

import com.admin.model.Feedback;
import com.admin.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public void save(Feedback feedback) {
        feedbackRepository.save(feedback);
    }
}