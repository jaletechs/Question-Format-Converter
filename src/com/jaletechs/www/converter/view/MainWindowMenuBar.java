package com.jaletechs.www.converter.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MainWindowMenuBar extends JMenuBar {
	public MainWindowMenuBar(){
		
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.showConfirmDialog(
						null,"Exit Now?","Exit Question Format Converter?", 
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);				
				if (answer == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}
		});
		
		fileMenu.add(exit);
		
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Designed By JALETECHS");
			}
			
		});
		
		add(fileMenu);
		add(about);
	}
}
