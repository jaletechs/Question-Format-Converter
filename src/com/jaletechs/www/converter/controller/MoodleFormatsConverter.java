package com.jaletechs.www.converter.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.jaletechs.www.converter.model.AikenQuestion;
import com.jaletechs.www.converter.util.AikenConstants;
import com.jaletechs.www.converter.util.MoodleXMLConstants;
import com.jaletechs.www.converter.view.MainWindow;

public class MoodleFormatsConverter {
	public static Document convertAikenToMoodleXML(File input){		
		MainWindow.logMessage("Verifying File Type...");
		if (input.getName().endsWith(".txt")) {
			MainWindow.logSuccessMessage("Verified!!");
		}else{
			MainWindow.logErrorMessage("Unknown File Type!!");
			return null;
		}
		
		MainWindow.logMessage("Reading From File...");
		
		try {			
			List<String> lines = FileUtils.readLines(input, "UTF-8");
			List<AikenQuestion> questions = new ArrayList<>();
			AikenQuestion question = new AikenQuestion();
			for (String line : lines){
				if(!line.isEmpty()){
					if (line.startsWith(AikenConstants.ANSWER)) {
						if(question != null){
							question.setCorrectOption(line.replaceFirst(AikenConstants.ANSWER, "").trim());
							questions.add(question);
						}
						question = new AikenQuestion();
					}else{
						if(line.startsWith(AikenConstants.OPTION_A) ||
								line.startsWith(AikenConstants.OPTION_A2)){
							question.getOptions().put(line.charAt(0)+"",line.replaceFirst(
									AikenConstants.OPTION_A, "").replaceFirst(
											"A\\)", "").trim());
						} else
						if(line.startsWith(AikenConstants.OPTION_B) ||
								line.startsWith(AikenConstants.OPTION_B2)){
							question.getOptions().put(line.charAt(0)+"",line.replaceFirst(
									AikenConstants.OPTION_B, "").replaceFirst(
											"B\\)", "").trim());
						} else
						if(line.startsWith(AikenConstants.OPTION_C) ||
								line.startsWith(AikenConstants.OPTION_C2)){
							question.getOptions().put(line.charAt(0)+"",line.replaceFirst(
									AikenConstants.OPTION_C, "").replaceFirst(
											"C\\)", "").trim());
						} else
						if(line.startsWith(AikenConstants.OPTION_D) ||
								line.startsWith(AikenConstants.OPTION_D2)){
							question.getOptions().put(line.charAt(0)+"",line.replaceFirst(
									AikenConstants.OPTION_D, "").replaceFirst(
											"D\\)", "").trim());
						} else
						if(line.startsWith(AikenConstants.OPTION_E) ||
								line.startsWith(AikenConstants.OPTION_E2)){
							question.getOptions().put(line.charAt(0)+"",line.replaceFirst(
									AikenConstants.OPTION_E, "").replaceFirst(
											"E\\)", "").trim());
						} else
						if(line.startsWith(AikenConstants.OPTION_F) ||
								line.startsWith(AikenConstants.OPTION_F2)){
							question.getOptions().put(line.charAt(0)+"",line.replaceFirst(
									AikenConstants.OPTION_F, "").replaceFirst(
											"F\\)", "").trim());
						} else
						if(line.startsWith(AikenConstants.OPTION_G) ||
								line.startsWith(AikenConstants.OPTION_G2)){
							question.getOptions().put(line.charAt(0)+"",line.replaceFirst(
									AikenConstants.OPTION_G, "").replaceFirst(
											"G\\)", "").trim());
						} else
						if(line.startsWith(AikenConstants.OPTION_H) ||
								line.startsWith(AikenConstants.OPTION_H2)){
							question.getOptions().put(line.charAt(0)+"",line.replaceFirst(
									AikenConstants.OPTION_H, "").replaceFirst(
											"H\\)", "").trim());
						} else
						if(line.startsWith(AikenConstants.OPTION_I) ||
								line.startsWith(AikenConstants.OPTION_I2)){
							question.getOptions().put(line.charAt(0)+"",line.replaceFirst(
									AikenConstants.OPTION_I, "").replaceFirst(
											"I\\)", "").trim());
						} else
						if(line.startsWith(AikenConstants.OPTION_J) ||
								line.startsWith(AikenConstants.OPTION_J2)){
							question.getOptions().put(line.charAt(0)+"",line.replaceFirst(
									AikenConstants.OPTION_J, "").replaceFirst(
											"J\\)", "").trim());
						} else {
							question.setQuestion(line);
						}
					}
				}
				
			}
			
			MainWindow.logMessage("Found " + questions.size() + " Aiken Questions!");
			
			return formMoodleXML(questions);
			
		} catch (IOException e) {
			MainWindow.logErrorMessage("IO Error Occured: " + e.getMessage());
		}
		
		return null;
	}

	private static Document formMoodleXML(List<AikenQuestion> questions) {
		Document doc = null;
		Element rootElement = new Element(MoodleXMLConstants.QUIZ);
		doc = new Document(rootElement);
		doc.setRootElement(rootElement);
		
		int count = 1;
		
		for (AikenQuestion question : questions){
			Element questionElement = new Element(MoodleXMLConstants.QUESTION);
			questionElement.setAttribute(new Attribute(MoodleXMLConstants.TYPE_ATTRIBUTE,
					MoodleXMLConstants.MULTI_CHOICE));
			Element nameElement = new Element(MoodleXMLConstants.NAME);
			Element nameTextElement = new Element(MoodleXMLConstants.TEXT);
			nameTextElement.setText(formatCount(count));
			count++;
			nameElement.addContent(nameTextElement);
			questionElement.addContent(nameElement);
			
			//Question text
			Element questionText = new Element(MoodleXMLConstants.QUESTION_TEXT);
			questionText.setAttribute(getNewHTMLFormatAttr());
			Element questionTextInner = new Element(MoodleXMLConstants.TEXT);
			questionTextInner.setText(question.getQuestion());
			questionText.addContent(questionTextInner);
			questionElement.addContent(questionText);
			
			//general feedback
			Element generalFeedbackElement = new Element(MoodleXMLConstants.GENERAL_FEEDBACK);
			generalFeedbackElement.setAttribute(getNewHTMLFormatAttr());
			generalFeedbackElement.addContent(new Element(MoodleXMLConstants.TEXT));
			questionElement.addContent(generalFeedbackElement);
			
			questionElement.addContent(new Element(MoodleXMLConstants.DEFAULT_GRADE).setText("1.0000000"));
			questionElement.addContent(new Element(MoodleXMLConstants.PENALTY).setText("0.3333333"));
			questionElement.addContent(new Element(MoodleXMLConstants.HIDDEN).setText("0"));
			questionElement.addContent(new Element(MoodleXMLConstants.SINGLE).setText("true"));
			questionElement.addContent(new Element(MoodleXMLConstants.SHUFFLE_ANSWERS).setText("true"));
			questionElement.addContent(new Element(MoodleXMLConstants.ANSWER_NUMBERING).setText("abc"));
			
			//correct feedback
			Element correctFeedbackElement = new Element(MoodleXMLConstants.CORRECT_FEEDBACK);
			correctFeedbackElement.setAttribute(getNewHTMLFormatAttr());
			correctFeedbackElement.addContent(new Element(MoodleXMLConstants.TEXT).setText(MoodleXMLConstants.CORRECT_FEEDBACK_TEXT));
			questionElement.addContent(correctFeedbackElement);
			
			//partially correct feedback
			Element partialFeedbackElement = new Element(MoodleXMLConstants.PARTIALLY_CORRECT_FEEDBACK);
			partialFeedbackElement.setAttribute(getNewHTMLFormatAttr());
			partialFeedbackElement.addContent(new Element(MoodleXMLConstants.TEXT).setText(MoodleXMLConstants.PARTIALLY_CORRECT_FEEDBACK_TEXT));
			questionElement.addContent(partialFeedbackElement);
			
			//partially correct feedback
			Element incorrectFeedbackElement = new Element(MoodleXMLConstants.INCORRECT_FEEDBACK);
			incorrectFeedbackElement.setAttribute(getNewHTMLFormatAttr());
			incorrectFeedbackElement.addContent(new Element(MoodleXMLConstants.TEXT).setText(MoodleXMLConstants.INCORRECT_FEEDBACK_TEXT));
			questionElement.addContent(incorrectFeedbackElement);
			
			questionElement.addContent(new Element(MoodleXMLConstants.SHOW_NUM_CORRECT));
			
			Map<String,String> options = question.getOptions();
			
			for(String option : options.keySet()){
				if (question.getCorrectOption() ==  null) {
					return null;
				}
				Element answerElement = new Element(MoodleXMLConstants.ANSWER);
				answerElement.setAttribute(getNewHTMLFormatAttr());
				
				if (option.equalsIgnoreCase(question.getCorrectOption()))
					answerElement.setAttribute(new Attribute(MoodleXMLConstants.FRACTION,"100"));
				else
					answerElement.setAttribute(new Attribute(MoodleXMLConstants.FRACTION,"0"));
				
				answerElement.addContent(new Element(MoodleXMLConstants.TEXT).setText(options.get(option)));
				
				Element feedbackElement = new Element(MoodleXMLConstants.FEEDBACK);
				feedbackElement.setAttribute(getNewHTMLFormatAttr());
				feedbackElement.addContent(new Element(MoodleXMLConstants.TEXT));
				answerElement.addContent(feedbackElement);
				
				questionElement.addContent(answerElement);				
			}
			
			doc.getRootElement().addContent(questionElement);
		}
		
		return doc;
	}
	
	private static Attribute getNewHTMLFormatAttr(){
		return new Attribute(MoodleXMLConstants.FORMAT_ATTRIBUTE,
				MoodleXMLConstants.HTML_FORMAT);
	}

	private static String formatCount(int count) {
		if (count < 10){
			return "0" + count+".";
		}else{
			return count+".";
		}
	}
	
	public static File convertMoodleXMLToAikenFormat(File xml){
		MainWindow.logMessage("Verifying File Type...");
		if (xml.getName().endsWith(".xml")) {
			MainWindow.logSuccessMessage("Verified!!");
		}else{
			MainWindow.logErrorMessage("Not an XML File!!");
			return null;
		}
		
		MainWindow.logMessage("Reading From File...");
		
		ArrayList<String> lines = new ArrayList<>();
		
		SAXBuilder builder = new SAXBuilder();
		
		try {
			Document doc = (Document) builder.build(xml);
			
			Element rootNode = doc.getRootElement();
			
			List<Element> children = rootNode.getChildren(MoodleXMLConstants.QUESTION);
			MainWindow.logMessage("Found " + children.size() + " Moodle XML Questions!");
			
			for (int i = 0; i < children.size(); i++){
				Element question = children.get(i);
				
				//add question
				lines.add(question.getChild(MoodleXMLConstants.QUESTION_TEXT)
						.getChild(MoodleXMLConstants.TEXT).getText());
				String correctAnswer = null;
				//add options
				
				List<Element> options = question.getChildren(MoodleXMLConstants.ANSWER);
				for (int j = 0; j < options.size(); j++){
					Element answer = (Element) options.get(j);
					
					String prefix = getNextOption(j);
					
					if(answer.getAttribute(MoodleXMLConstants.FRACTION).getValue().equals("100")){
						correctAnswer = prefix.replaceAll("\\.", "");
					}
										
					lines.add(prefix + " " + answer.getChild(MoodleXMLConstants.TEXT).getText());
				}
				
				lines.add(AikenConstants.ANSWER + " " + correctAnswer);
				//empty line
				lines.add("");
			}
			
			File temp = File.createTempFile("temp", ".txt");
			FileUtils.writeLines(temp, lines,true);
			
			return temp;
		}catch (IOException e){
			MainWindow.logErrorMessage(e.getMessage());
		}catch (JDOMException e){
			MainWindow.logErrorMessage(e.getMessage());
		}
		
		return null;
	}

	private static String getNextOption(int j) {
		return AikenConstants.OPTIONS[j];
	}
}
