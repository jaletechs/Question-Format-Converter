package com.jaletechs.www.converter.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jaletechs.www.converter.controller.CrossPlatformFormatController;
import com.jaletechs.www.converter.model.QuestionDocumentFormat;

public class CrossPlatformFormatView extends JPanel implements CrossPlatformFormatController.Display{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<QuestionDocumentFormat> from,to;
	private JButton convert;
	private JFileChooser inputDirectory, outputDirectory;
	private JTextField inputDirectoryField, outDirectoryField;
	private JButton chooseInputFile, chooseOutputDirectory;
	private JPanel finalPanel;
	
	public CrossPlatformFormatView(){
		setBorder(BorderFactory.createTitledBorder("Cross Platform Formats"));
		
		finalPanel = new JPanel();
		//finalPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		finalPanel.setLayout(new BoxLayout(finalPanel, BoxLayout.Y_AXIS));
		JLabel icon = new JLabel(new ImageIcon(getClass().getResource("cross.png")));
		//icon.setText("oodle");
		JPanel iconPanel = new JPanel();
		iconPanel.add(icon);
		finalPanel.add(iconPanel);
		
		
		inputDirectoryField = new JTextField(25);
		inputDirectoryField.setPreferredSize(new Dimension(20,30));
		inputDirectoryField.setEditable(false);
		outDirectoryField = new JTextField(25);
		outDirectoryField.setPreferredSize(new Dimension(20,30));
		outDirectoryField.setEditable(false);
		chooseInputFile = new JButton("Select Question File", new ImageIcon(getClass().getResource("file.png")));
		from = new JComboBox<>();
		to = new JComboBox<>();
		convert = new JButton("Convert", new ImageIcon(getClass().getResource("convert.png")));
		chooseOutputDirectory = new JButton("Choose Output Folder", new ImageIcon(getClass().getResource("folder.png")));
		inputDirectory = new JFileChooser();
		inputDirectory.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		//inputDirectory.addChoosableFileFilter(new FileNameExtensionFilter("Aiken Format (.txt)", "txt"));
		inputDirectory.addChoosableFileFilter(new FileNameExtensionFilter("Moodle XML (.xml)", "xml"));
		
		outputDirectory = new JFileChooser();
		outputDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JPanel inputFilePanel = new JPanel();
		inputFilePanel.add(inputDirectoryField);
		inputFilePanel.add(chooseInputFile);
		
		JPanel formatsPanel = new JPanel();
		formatsPanel.setBorder(BorderFactory.createTitledBorder("Cross Platform Formats"));
		formatsPanel.setLayout(new GridLayout(3, 2));
		formatsPanel.add(new JLabel("From"));
		formatsPanel.add(from);
		formatsPanel.add(new JPanel());
		formatsPanel.add(new JPanel());
		formatsPanel.add(new JLabel("To"));
		formatsPanel.add(to);
		
		JPanel outPanel = new JPanel();
		outPanel.add(outDirectoryField);
		outPanel.add(chooseOutputDirectory);
		
		JPanel convertPanel = new JPanel();
		convertPanel.add(convert);
		
		
		finalPanel.add(inputFilePanel);
		finalPanel.add(formatsPanel);
		finalPanel.add(outPanel);		
		finalPanel.add(convertPanel);
		
		add(finalPanel);
		
		CrossPlatformFormatController controller = new CrossPlatformFormatController(this);
	}
	
	@Override
	public JComboBox<QuestionDocumentFormat> getFromBox() {
		return from;
	}

	@Override
	public JComboBox<QuestionDocumentFormat> getToBox() {
		return to;
	}

	@Override
	public JButton getConvertButton() {
		return convert;
	}

	@Override
	public JFileChooser getInputDirectoryChooser() {
		return inputDirectory;
	}

	@Override
	public JFileChooser getOutputDirectoryChooser() {
		return outputDirectory;
	}

	@Override
	public JTextField getInputDirectoryField() {
		return inputDirectoryField;
	}

	@Override
	public JTextField getOutputDirectoryField() {
		return outDirectoryField;
	}

	@Override
	public JButton getChooseInputFileButton() {
		return chooseInputFile;
	}

	@Override
	public JButton getChooseOutputDirectoryButton() {
		return chooseOutputDirectory;
	}

	@Override
	public JPanel getParentPanel() {
		return finalPanel;
	}
}
