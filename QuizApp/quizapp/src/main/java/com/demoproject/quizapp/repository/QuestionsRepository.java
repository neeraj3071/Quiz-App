package com.demoproject.quizapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demoproject.quizapp.model.Questions;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Integer>{

	List<Questions> findByCategory(String category);
	boolean existsByQuestionTitle(String questionTitle);
}
