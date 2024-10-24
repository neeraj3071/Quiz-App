package com.demoproject.dto;

import lombok.Data;

@Data
public class CreateUpdateRequest {
    private Integer id; 
    private String questionTitle;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String rightAnswer;
    private String difficultyLevel;
    private String category;
}
