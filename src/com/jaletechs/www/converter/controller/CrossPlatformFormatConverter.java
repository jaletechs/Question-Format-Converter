package com.jaletechs.www.converter.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;

import com.jaletechs.www.converter.model.EasyJQuestion;
import com.jaletechs.www.converter.util.EasyJConstants;
import com.jaletechs.www.converter.util.MoodleXMLConstants;
import com.jaletechs.www.converter.view.MainWindow;

public class CrossPlatformFormatConverter {

	public static File convertToEasyJFormat(File xml) {
		
		MainWindow.logMessage("Verifying File Type...");
		if (xml.getName().endsWith(".xml")) {
			MainWindow.logSuccessMessage("Verified!!");
		}else{
			MainWindow.logErrorMessage("Not an XML File!!");
			return null;
		}
		
		MainWindow.logMessage("Reading From File...");
		
		List<EasyJQuestion> questions = new ArrayList<>();
		SAXBuilder builder = new SAXBuilder();
		
		try {
			Document doc = (Document) builder.build(xml);
			
			Element rootNode = doc.getRootElement();
			List<Element> children = rootNode.getChildren(MoodleXMLConstants.QUESTION);
			MainWindow.logMessage("Found " + children.size() + " Moodle XML Questions!");
			
			for (int i = 0; i < children.size(); i++){
				EasyJQuestion current = new EasyJQuestion();
				Element question = children.get(i);
				if(!question.getAttribute(MoodleXMLConstants.TYPE_ATTRIBUTE).
						getValue().equals(MoodleXMLConstants.CATEGORY)){
					//add question
					current.setQuestionText((question.getChild(MoodleXMLConstants.QUESTION_TEXT)
							.getChild(MoodleXMLConstants.TEXT).getText()));
					
					current.setRandomized(Boolean.valueOf(question
							.getChild(MoodleXMLConstants.SHUFFLE_ANSWERS).getText()));
					
					if(question.getAttribute(MoodleXMLConstants.TYPE_ATTRIBUTE)
							.getValue().equals(MoodleXMLConstants.MULTI_CHOICE)){
						current.setType(EasyJConstants.NORMAL);
					}
					
					Double defaultGrade = Double.parseDouble(question
							.getChild(MoodleXMLConstants.DEFAULT_GRADE).getText().trim());
					
					current.setMark(defaultGrade.intValue());
					
					String imageString = null;
					Element base64Element = question.getChild(MoodleXMLConstants.BASE_64_IMAGE);
					if (base64Element != null){
						if(base64Element.getText() != null && !base64Element.getText().isEmpty()){
							current.setQuestionImage(base64Element.getText());
							current.setImageName(question.getChild(MoodleXMLConstants.IMAGE).getText());
						}
					}
					
					String correctAnswer = null;
					//add options
					
					List<Element> options = question.getChildren(MoodleXMLConstants.ANSWER);
					for (int j = 0; j < options.size(); j++){
						Element answer = (Element) options.get(j);
						String prefix = "";
						if(j<=4)
							prefix = getNextOption(j);
						
						if(answer.getAttribute(MoodleXMLConstants.FRACTION).getValue().equals("100")){
							correctAnswer = prefix.replaceAll("\\.", "");
						}
											
						mapToOption(prefix,answer.getChild(MoodleXMLConstants.TEXT).getText(),current);
					}
					
					current.setCorrectOption(correctAnswer);
					questions.add(current);
				}
			}
			
			File wordDoc = formEasyJDocument(questions);
			
			return wordDoc;
			
		}catch (IOException e){
			MainWindow.logErrorMessage(e.getMessage());
		}catch (JDOMException e){
			MainWindow.logErrorMessage(e.getMessage());
		}
		
		return null;
	}
	
	private static File formEasyJDocument(List<EasyJQuestion> questions) throws IOException {
		File file = File.createTempFile("temp", ".docx");
		XWPFDocument document = new XWPFDocument();
		
		XWPFParagraph subjectParagraph = document.createParagraph();
		subjectParagraph.createRun().setText(EasyJConstants.FORM);
		subjectParagraph.createRun().addBreak();
		subjectParagraph.createRun().setText(EasyJConstants.SUBJECT_CODE);
		subjectParagraph.createRun().addBreak();
		subjectParagraph.createRun().setText(EasyJConstants.SUBJECT_NAME);
		
		for (EasyJQuestion question : questions){
			
			XWPFParagraph questionParagraph = document.createParagraph();
			
			questionParagraph.createRun().setText(EasyJConstants.QUESTION + " " + question.getQuestionText());
			questionParagraph.createRun().addBreak();
			questionParagraph.createRun().setText(EasyJConstants.OPTION_A + " " + question.getOptionA());
			questionParagraph.createRun().addBreak();
			questionParagraph.createRun().setText(EasyJConstants.OPTION_B + " " + question.getOptionB());
			questionParagraph.createRun().addBreak();
			questionParagraph.createRun().setText(EasyJConstants.OPTION_C + " " + question.getOptionC());
			questionParagraph.createRun().addBreak();
			questionParagraph.createRun().setText(EasyJConstants.OPTION_D + " " + question.getOptionD());
			questionParagraph.createRun().addBreak();
			
			if(question.getOptionE() != null){
				questionParagraph.createRun().setText(EasyJConstants.OPTION_E + " " + question.getOptionE());
				questionParagraph.createRun().addBreak();
			}
			
			questionParagraph.createRun().setText(EasyJConstants.CORRECT_OPTION+ " " + question.getCorrectOption());
			questionParagraph.createRun().addBreak();
			questionParagraph.createRun().setText(EasyJConstants.DIFFICULTY + " " + question.getMark());
			questionParagraph.createRun().addBreak();
			questionParagraph.createRun().setText(EasyJConstants.TYPE + " " + question.getType());
			questionParagraph.createRun().addBreak();
			questionParagraph.createRun().setText(EasyJConstants.RANDOMIZED + " " + 
					String.valueOf(question.isRandomized()).toUpperCase());
			questionParagraph.createRun().addBreak();
			if (question.getQuestionImage() != null){
				questionParagraph.createRun().setText(EasyJConstants.IMAGE);
				byte [] data = Base64.getDecoder().decode(question.getQuestionImage());
				File imageFile = File.createTempFile(question.getImageName(),"");
				try(OutputStream stream =  new FileOutputStream(imageFile)){
					stream.write(data);
				}
							
				try {
						if (question.getImageName().endsWith(".png")){
							String blipId = document.addPictureData(new FileInputStream(imageFile), 
									XWPFDocument.PICTURE_TYPE_PNG);
							createPicture(document,blipId,document.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG), 100, 100);
							
						}
						else if(question.getImageName().endsWith(".jpg")){
							String blipId = document.addPictureData(new FileInputStream(imageFile), 
									XWPFDocument.PICTURE_TYPE_JPEG);
							
							createPicture(document,blipId,document.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_JPEG), 100, 100);
						}
					} catch (InvalidFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		FileOutputStream fos = new FileOutputStream(file);
		document.write(fos);
		
		fos.close();
		return file;
	}

	private static void mapToOption(String prefix, String text, EasyJQuestion question) {
		if(prefix.equals(EasyJConstants.OPTION_A)){
			question.setOptionA(text);
		}
		
		if(prefix.equals(EasyJConstants.OPTION_B)){
			question.setOptionB(text);
		}
		
		if(prefix.equals(EasyJConstants.OPTION_C)){
			question.setOptionC(text);
		}
		
		if(prefix.equals(EasyJConstants.OPTION_D)){
			question.setOptionD(text);
		}
		
		if(prefix.equals(EasyJConstants.OPTION_E)){
			question.setOptionE(text);
		}
	}

	private static String getNextOption(int j) {
		return EasyJConstants.OPTIONS[j];
	}
	
	private static void createPicture(XWPFDocument doc, String blipId,int id, int width, int height)
    {
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;

        CTInline inline = doc.createParagraph().createRun().getCTR().addNewDrawing().addNewInline();

        String picXml = "" +
                "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
                "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                "         <pic:nvPicPr>" +
                "            <pic:cNvPr id=\"" + id + "\" name=\"Generated\"/>" +
                "            <pic:cNvPicPr/>" +
                "         </pic:nvPicPr>" +
                "         <pic:blipFill>" +
                "            <a:blip r:embed=\"" + blipId + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" +
                "            <a:stretch>" +
                "               <a:fillRect/>" +
                "            </a:stretch>" +
                "         </pic:blipFill>" +
                "         <pic:spPr>" +
                "            <a:xfrm>" +
                "               <a:off x=\"0\" y=\"0\"/>" +
                "               <a:ext cx=\"" + width + "\" cy=\"" + height + "\"/>" +
                "            </a:xfrm>" +
                "            <a:prstGeom prst=\"rect\">" +
                "               <a:avLst/>" +
                "            </a:prstGeom>" +
                "         </pic:spPr>" +
                "      </pic:pic>" +
                "   </a:graphicData>" +
                "</a:graphic>";

        XmlToken xmlToken = null;
        try
        {
            xmlToken = XmlToken.Factory.parse(picXml);
        }
        catch(XmlException xe)
        {
            xe.printStackTrace();
        }
        inline.set(xmlToken);

        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);

        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);

        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("Picture " + id);
        docPr.setDescr("Generated");
    }
}
