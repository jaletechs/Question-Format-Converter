package com.jaletechs.www.converter.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class AikenQuestion extends QuestionDocument{
	private String question;
	private Map<String,String> options;
	private String correctOption;
	
	public AikenQuestion(){
		this.options = new LinkedHashMap<>();
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Map<String,String> getOptions() {
		return options;
	}

	public void setOptions(Map<String,String> options) {
		this.options = options;
	}

	public String getCorrectOption() {
		return correctOption;
	}

	public void setCorrectOption(String correctOption) {
		this.correctOption = correctOption;
	}
}
