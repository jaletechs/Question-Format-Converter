
/*
 * This class creates Question Format Objects for use in conversion
 * For Moodle Question Formats, Aiken Format and Moodle XML Format is returned
 * For the Cross Platform Conversion, A mix of Both Aiken Format and Other Format is returned
 */
package com.jaletechs.www.converter.util;

import java.util.ArrayList;
import java.util.List;

import com.jaletechs.www.converter.model.QuestionDocumentFormat;
/*
 * @author jaletechs
 */

public class QuestionDocumentFormatFactory {
	public static List<QuestionDocumentFormat> getMoodleFormats(){
		List<QuestionDocumentFormat> formats = new ArrayList<>();
		
		QuestionDocumentFormat aiken = new QuestionDocumentFormat(
				QuestionFormat.AIKEN.toString(), "Aiken Format", "Aiken Format");
		QuestionDocumentFormat xml = new QuestionDocumentFormat(
				QuestionFormat.MOODLE_XML.toString(), "Moodle XML","Moodle XML");
		
		formats.add(aiken);
		formats.add(xml);
		
		return formats;
	}
	
	public static QuestionDocumentFormat getAikenFormat(){
		return new QuestionDocumentFormat(
				QuestionFormat.AIKEN.toString(), "Aiken Format", "Aiken Format");
	}
	
	public static QuestionDocumentFormat getMoodleXMLFormat(){
		return new QuestionDocumentFormat(
				QuestionFormat.MOODLE_XML.toString(), "Moodle XML","Moodle XML");
	}
	
	public static QuestionDocumentFormat getEasyJFormat(){
		return new QuestionDocumentFormat(
				QuestionFormat.EASYJ.toString(),"Easy J","Easy J");
	}
}
