package com.jaletechs.www.converter.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import com.javaswingcomponents.accordion.JSCAccordion;
import com.javaswingcomponents.accordion.TabOrientation;
import com.javaswingcomponents.accordion.plaf.AccordionUI;
import com.javaswingcomponents.accordion.plaf.darksteel.DarkSteelAccordionUI;
import com.javaswingcomponents.framework.painters.configurationbound.GradientColorPainter;

public class MainWindow {
	private JFrame frame;
	private JSCAccordion accordion;
	private static JTextArea status;
	
	public static void main(String [] args){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try{
					//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}catch(Exception e){e.printStackTrace();}
				MainWindow window = new MainWindow();
				window.go();
			}
		});
	}
	
	public void go(){
		frame = new JFrame("Question Format Converter");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent ev){
				int answer = JOptionPane.showConfirmDialog(
						frame,"Exit Now?","Exit Question Format Converter?", 
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);				
				if (answer == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}
		});
		
		JPanel finalPanel = new JPanel();
		finalPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		//accordion 
		accordion = initAccordion();
		accordion.setPreferredSize(new Dimension(530,480));
		accordion.setBorder(BorderFactory.createTitledBorder("Conversion Menu"));
		JPanel accordionPanel = new JPanel();
		
		accordionPanel.add(accordion);
		finalPanel.add(accordionPanel);
		
		status = new JTextArea();
		status.setEditable(false);
		status.setWrapStyleWord(true);
		status.setLineWrap(true);
		status.setDisabledTextColor(Color.GRAY);
		status.append("============ Conversion ===========");
		JScrollPane scroller = new JScrollPane(status);
		scroller.setPreferredSize(new Dimension(300,480));
		scroller.setBorder(BorderFactory.createTitledBorder("Status"));
		finalPanel.add(scroller);
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screen = kit.getScreenSize();
		
		frame.getContentPane().add(finalPanel, BorderLayout.CENTER);
		frame.setJMenuBar(new MainWindowMenuBar());
		frame.setSize(800,550);
		frame.pack();
		frame.setLocation(screen.width/4, screen.height/8);
		frame.setVisible(true);
	}
	
	private JSCAccordion initAccordion(){
		JSCAccordion temp = new JSCAccordion();
		
		temp.addTab("Moodle Formats", new MoodleFormatView());
		temp.addTab("Cross Platform Formats", new CrossPlatformFormatView());
		
		temp.setTabOrientation(TabOrientation.VERTICAL);
		
		AccordionUI newUI = DarkSteelAccordionUI.createUI(temp);
		//We set the UI
		temp.setUI(newUI);
		
		DarkSteelAccordionUI ui = (DarkSteelAccordionUI) temp.getUI();
		ui.setHorizontalBackgroundPadding(10);
		
		GradientColorPainter backgroundPainter = (GradientColorPainter) temp.getBackgroundPainter();
		backgroundPainter = (GradientColorPainter) temp.getBackgroundPainter();
		backgroundPainter.setStartColor(Color.BLUE);
		backgroundPainter.setEndColor(Color.GREEN);
		
		return temp;
	}
	
	public static void logMessage(String message){
		status.setForeground(Color.BLACK);
		status.append("\r\n" + message);
	}
	
	public static void clearAndLogMessage(String message){
		clearStatus();
		status.setForeground(Color.BLACK);
		status.append("\r\n" + message);
	}
	
	public static void logErrorMessage(String message){
		status.setForeground(Color.RED);
		status.append("\r\n" + message);
	}
	
	public static void clearAndLogErrorMessage(String message){
		clearStatus();
		status.setForeground(Color.RED);
		status.append("\r\n" + message);
	}
	
	public static void logSuccessMessage(String message){
		status.setForeground(Color.GREEN);
		status.append("\r\n" + message);
	}
	
	public static void clearStatus(){
		status.setText("");
	}
}
