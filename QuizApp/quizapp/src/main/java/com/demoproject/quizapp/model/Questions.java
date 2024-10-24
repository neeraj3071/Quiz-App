package com.demoproject.quizapp.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
@Data
@Table(name = "question")  
public class Questions {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @Column(nullable = false)
	    private String questionTitle;

	    @Column(nullable = false)
	    private String option1;

	    @Column(nullable = false)
	    private String option2;

	    @Column(nullable = false)
	    private String option3;

	    @Column(nullable = false)
	    private String option4;

	    @Column(nullable = false)
	    private String rightAnswer;

	    @Column(nullable = false)
	    private String difficultyLevel;

	    @Column(nullable = false)
	    private String category;
	

}
