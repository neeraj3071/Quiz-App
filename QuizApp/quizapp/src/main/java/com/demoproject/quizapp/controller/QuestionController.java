package com.demoproject.quizapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.demoproject.dto.CreateUpdateRequest;
import com.demoproject.dto.DeleteRequest;
import com.demoproject.quizapp.service.QuestionService;
import com.demoproject.dto.ApiResponse; // Import the custom ApiResponse class
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("questions")
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;


    @PostMapping("fetchQuestions")
    public ResponseEntity<ApiResponse<?>> fetchQuestions(@RequestParam(required = false) String category) {
        logger.info("Fetching questions with category: {}", category);
        ApiResponse<?> response = questionService.getQuestionsByCategory(category);
        return ResponseEntity.ok(response);
    }


    @PostMapping("create")
    public ResponseEntity<ApiResponse<?>> createQuestion(@RequestBody CreateUpdateRequest request) {
        logger.info("Creating a new question with title: {}", request.getQuestionTitle());
        ApiResponse<?> response = questionService.createQuestion(request);
        return ResponseEntity.status(response.getMessage().equals("Question created successfully") ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    
    @PutMapping("update")
    public ResponseEntity<ApiResponse<?>> updateQuestion(@RequestBody CreateUpdateRequest request) {
        logger.info("Updating question with ID: {}", request.getId());
        ApiResponse<?> response = questionService.updateQuestion(request);
        return ResponseEntity.ok(response);
    }

   
    @DeleteMapping("delete")
    public ResponseEntity<ApiResponse<?>> deleteQuestion(@RequestBody DeleteRequest request) {
        logger.info("Deleting question with ID: {}", request.getId());
        ApiResponse<?> response = questionService.deleteQuestion(request.getId());
        return ResponseEntity.ok(response);
    }
}
