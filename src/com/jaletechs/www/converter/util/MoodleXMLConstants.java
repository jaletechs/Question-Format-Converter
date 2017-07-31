package com.jaletechs.www.converter.util;

/*
*Constants as seen in Moodle XML Tags
*/

public interface MoodleXMLConstants {
	String QUESTION = "question";
	String NAME = "name";
	String TEXT = "text";
	String SINGLE = "single";
	String QUESTION_TEXT = "questiontext";
	String MULTI_CHOICE = "multichoice";
	String TRUE_FALSE = "truefalse";
	String SHORT_ANSWER = "shortanswer";
	String TYPE_ATTRIBUTE = "type";
	String FORMAT_ATTRIBUTE = "format";
	String HTML_FORMAT = "html";
	String QUIZ = "quiz";
	String OPEN_CDATA = "<![CDATA[";
	String CLOSE_CDATA = "]]>";
	String CATEGORY = "category";
	String GENERAL_FEEDBACK = "generalfeedback";
	String DEFAULT_GRADE = "defaultgrade";
	String PENALTY = "penalty";
	String HIDDEN = "true";
	String SHUFFLE_ANSWERS = "shuffleanswers";
	String ANSWER_NUMBERING = "answernumbering";
	String CORRECT_FEEDBACK = "correctfeedback";
	String PARTIALLY_CORRECT_FEEDBACK = "partiallycorrectfeedback";
	String INCORRECT_FEEDBACK = "incorrectfeedback";
	String FEEDBACK = "feedback";
	String ANSWER = "answer";
	String FRACTION = "fraction";
	
	String CORRECT_FEEDBACK_TEXT = "Your answer is correct.";
	String INCORRECT_FEEDBACK_TEXT = "Your answer is incorrect.";
	String PARTIALLY_CORRECT_FEEDBACK_TEXT = "Your answer is partially correct.";
	String SHOW_NUM_CORRECT = "shownumcorrect";
	String IMAGE = "image";
	String BASE_64_IMAGE = "image_base64";
}
