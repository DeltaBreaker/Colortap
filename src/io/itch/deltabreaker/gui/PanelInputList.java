package io.itch.deltabreaker.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PanelInputList extends JPanel {

	private static final long serialVersionUID = -8054849035862063447L;
	
	private ArrayList<PanelInput> inputList = new ArrayList<>();
	
	public PanelInputList() {
		setLayout(null);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	public PanelInput getInput(int i) {
		return inputList.get(i);
	}
	
	public PanelInput[] getInputList() {
		return inputList.toArray(new PanelInput[inputList.size()]);
	}
	
	public void addInput(PanelInput input) {
		input.setLocation(5, 5 + inputList.size() * 87);
		inputList.add(input);
		add(input);
		setSize(185, 5 + inputList.size() * 87);
		setPreferredSize(new Dimension(getSize()));
	}
	
	public void removeInput(PanelInput input) {
		if(inputList.contains(input)) {
			inputList.remove(input);
			removeAll();
			for(int i = 0; i < inputList.size(); i++) {
				inputList.get(i).setLocation(5, 5 + i * 87);
				add(inputList.get(i));
			}
			setSize(185, 5 + inputList.size() * 87);
			setPreferredSize(new Dimension(getSize()));
			repaint();
		}
	}
	
}
