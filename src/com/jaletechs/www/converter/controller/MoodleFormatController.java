package com.jaletechs.www.converter.controller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.jaletechs.www.converter.model.QuestionDocumentFormat;
import com.jaletechs.www.converter.util.QuestionDocumentFormatFactory;
import com.jaletechs.www.converter.view.MainWindow;

public class MoodleFormatController {
	
	private Display display;
	
	public interface Display {
		JPanel getParentPanel();
		JComboBox<QuestionDocumentFormat> getFromBox();
		JComboBox<QuestionDocumentFormat> getToBox();
		JButton getConvertButton();
		JFileChooser getInputDirectoryChooser();
		JFileChooser getOutputDirectoryChooser();
		JTextField getInputDirectoryField();
		JTextField getOutputDirectoryField();
		JButton getChooseInputFileButton();
		JButton getChooseOutputDirectoryButton();
	}
	
	public MoodleFormatController(Display display){
		this.display = display;
		fetchData();
		bind();
	}
	
	private void fetchData(){
		List<QuestionDocumentFormat> formats = 
				QuestionDocumentFormatFactory.getMoodleFormats();
		
		for (QuestionDocumentFormat format : formats){
			display.getFromBox().addItem(format);
			display.getToBox().addItem(format);
		}
	}
	
	public void bind(){
		display.getConvertButton().addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(display.getFromBox().getSelectedItem().equals(display.getToBox().getSelectedItem())){
					JOptionPane.showMessageDialog(display.getParentPanel(), 
							"Cannot convert to the same Format!");
					return;
				}
				if (getSelectedInputFile() == null) {
					JOptionPane.showMessageDialog(display.getParentPanel(), 
							"Select input file");
					return;
				}
				File input = getSelectedInputFile();
				String outputDirectory = getOuputDirectory();
				
				if (outputDirectory==null || outputDirectory.isEmpty()) {
					JOptionPane.showMessageDialog(display.getParentPanel(), 
							"Select output folder");
					return;
				}
				
				/*
				 * perform conversion;
				 */
				
				if (display.getToBox().getSelectedItem().equals(QuestionDocumentFormatFactory.getMoodleXMLFormat())) {
					File file = new File(outputDirectory + 
							File.separator + input.getName().replaceAll(".txt", "")+".xml");
					
					Document doc = null;
					doc = MoodleFormatsConverter.convertAikenToMoodleXML(input);
					try {
						if(doc == null){
							JOptionPane.showMessageDialog(display.getParentPanel(), 
									"Error In Conversion!");
							return;
						}
						XMLOutputter xml = new XMLOutputter();
						xml.setFormat(Format.getPrettyFormat());
						xml.output(doc, new FileOutputStream(file));
						
						Desktop.getDesktop().open(new File(outputDirectory));
						
						MainWindow.logSuccessMessage("Conversion Completed Successfully!");
					} catch (IOException e) {
						MainWindow.logErrorMessage(e.getMessage());
					}
				}else{
					File outputFile = new File(outputDirectory + 
							File.separator + input.getName().replaceAll(".xml", "")+".txt");
					
					File file = MoodleFormatsConverter.convertMoodleXMLToAikenFormat(input);
					if (file == null){
						JOptionPane.showMessageDialog(display.getParentPanel(), 
								"Error In Conversion!");
						return;
					}
					try {
						
						FileUtils.copyFile(file, outputFile);
						Desktop.getDesktop().open(new File(outputDirectory));
						
						MainWindow.logSuccessMessage("Conversion Completed Successfully!");
					}catch (IOException e){
						
					}
					
				}
		}
		});
		
		display.getChooseInputFileButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = display.getInputDirectoryChooser().showOpenDialog(display.getParentPanel());
				if (choice == JFileChooser.APPROVE_OPTION) {
					display.getInputDirectoryField().setText(getSelectedInputFile().getAbsolutePath());
					
					MainWindow.logMessage("** File Name: " +  getSelectedInputFile().getName());
				}
			}
		});
		
		display.getChooseOutputDirectoryButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = display.getOutputDirectoryChooser().showOpenDialog(display.getParentPanel());
				if (choice == JFileChooser.APPROVE_OPTION){
					display.getOutputDirectoryField().setText(getOuputDirectory());
					MainWindow.logMessage("** Output File: " +  getOuputDirectory());
				}
			}
		});
	}
	
	private File getSelectedInputFile(){
		return display.getInputDirectoryChooser().getSelectedFile();
	}
	
	private String getOuputDirectory(){
		String path = "";
		try {
			path = display.getOutputDirectoryChooser().getSelectedFile().toString();
		} catch (Exception e) {
			MainWindow.logErrorMessage("An IO Error Occured: " + e.getMessage());
		}
		return path;
	}
}
