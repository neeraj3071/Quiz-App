package com.demoproject.quizapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.demoproject.quizapp.model.Questions;
import com.demoproject.quizapp.repository.QuestionsRepository;
import com.demoproject.dto.CreateUpdateRequest;
import com.demoproject.quizapp.exception.DataNotFoundException;
import com.demoproject.quizapp.exception.InvalidInputException;
import com.demoproject.dto.ApiResponse;  // Import the custom response class
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private QuestionsRepository questionsRepository;

    public ApiResponse<List<Questions>> getAllQuestions() {
        logger.info("Fetching all questions");
        List<Questions> questions = questionsRepository.findAll();
        if (questions.isEmpty()) {
            logger.warn("No questions available in the database");
            return new ApiResponse<>("No questions found", null);
        }

        return new ApiResponse<>("Questions retrieved successfully", questions);
    }

    public ApiResponse<List<Questions>> getQuestionsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            logger.warn("No category provided; returning all questions");
            return getAllQuestions();
        }

        logger.info("Fetching questions for category: {}", category);
        List<Questions> questions = questionsRepository.findByCategory(category);
        if (questions.isEmpty()) {
            logger.warn("No questions found for category: {}", category);
            return new ApiResponse<>("No questions found for category: " + category, null);
        }

        return new ApiResponse<>("Questions retrieved successfully for category: " + category, questions);
    }

    public ApiResponse<Questions> createQuestion(CreateUpdateRequest request) {
        validateCreateUpdateRequest(request, false);

        logger.info("Checking if a question with the title '{}' already exists", request.getQuestionTitle());

        if (questionsRepository.existsByQuestionTitle(request.getQuestionTitle())) {
            logger.warn("A question with the title '{}' already exists", request.getQuestionTitle());
            throw new InvalidInputException("A question with the same title already exists");
        }

        logger.info("Creating a new question with title: {}", request.getQuestionTitle());
        Questions question = new Questions();
        mapRequestToQuestion(request, question);
        Questions savedQuestion = questionsRepository.save(question);
        logger.info("Question created with ID: {}", savedQuestion.getId());

        return new ApiResponse<>("Question created successfully", savedQuestion);
    }

    public ApiResponse<Questions> updateQuestion(CreateUpdateRequest request) {
        validateCreateUpdateRequest(request, true);

        logger.info("Updating question with ID: {}", request.getId());
        Questions existingQuestion = questionsRepository.findById(request.getId())
                .orElseThrow(() -> {
                    logger.error("Question not found with ID: {}", request.getId());
                    return new DataNotFoundException("Question not found with ID: " + request.getId());
                });

        mapRequestToQuestion(request, existingQuestion);

        Questions updatedQuestion = questionsRepository.save(existingQuestion);
        logger.info("Question updated with ID: {}", updatedQuestion.getId());

        return new ApiResponse<>("Question updated successfully", updatedQuestion);
    }

    public ApiResponse<String> deleteQuestion(Integer id) {
        if (id == null || id <= 0) {
            logger.error("Invalid question ID: {}", id);
            return new ApiResponse<>("Invalid question ID: " + id, null);
        }

        logger.info("Deleting question with ID: {}", id);
        Questions existingQuestion = questionsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Question not found with ID: {}", id);
                    return new DataNotFoundException("Question not found with ID: " + id);
                });

        questionsRepository.delete(existingQuestion);
        logger.info("Question deleted with ID: {}", id);

        return new ApiResponse<>("Question deleted successfully", null);
    }

    private void validateCreateUpdateRequest(CreateUpdateRequest request, boolean isUpdate) {
        if (request == null) {
            logger.error("Request body is null");
            throw new InvalidInputException("Request body cannot be null");
        }

        if (isUpdate && (request.getId() == null || request.getId() <= 0)) {
            logger.error("Invalid ID provided for updating: {}", request.getId());
            throw new InvalidInputException("Invalid question ID for update");
        }

        if (request.getQuestionTitle() == null || request.getQuestionTitle().trim().isEmpty()) {
            logger.error("Question title is missing");
            throw new InvalidInputException("Question title cannot be empty");
        }

        if (request.getRightAnswer() == null || request.getRightAnswer().trim().isEmpty()) {
            logger.error("Right answer is missing");
            throw new InvalidInputException("Right answer cannot be empty");
        }

        logger.info("Validation passed for create/update request");
    }

    private void mapRequestToQuestion(CreateUpdateRequest request, Questions question) {
        question.setQuestionTitle(request.getQuestionTitle());
        question.setOption1(request.getOption1());
        question.setOption2(request.getOption2());
        question.setOption3(request.getOption3());
        question.setOption4(request.getOption4());
        question.setRightAnswer(request.getRightAnswer());
        question.setDifficultyLevel(request.getDifficultyLevel());
        question.setCategory(request.getCategory());
    }
}
