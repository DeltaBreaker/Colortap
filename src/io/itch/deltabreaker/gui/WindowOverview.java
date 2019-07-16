package io.itch.deltabreaker.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import io.itch.deltabreaker.automation.InputRelayThread;
import io.itch.deltabreaker.main.FileHandler;
import io.itch.deltabreaker.main.StartupColortap;

public class WindowOverview extends JFrame {

	private static final long serialVersionUID = 6681253289808976461L;
	private static final String title = "Colortap";
	private static final Border border = BorderFactory.createLineBorder(WindowOverview.accent);

	public static final Color theme = new Color(238, 238, 238);
	public static final Color accent = new Color(40, 40, 40);

	private JScrollPane scrollingInputList;
	private JButton record, save, load;
	private JLabel pollDelayLabel, clickDeviationLabel;
	private JTextArea file;
	
	public InputRelayThread thread = new InputRelayThread();
	public JTextArea pollDelay, clickDeviation;
	public JButton start;
	public PanelInputList inputList;

	public WindowOverview() {
		super(title);
		thread.isRunning = false;

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(320, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);

		inputList = new PanelInputList();
		scrollingInputList = new JScrollPane(inputList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollingInputList.setBounds(0, 0, 205, getHeight() - 29);
		scrollingInputList.setBorder(border);

		record = new JButton("Record");
		record.setBounds(210, 5, 100, 20);
		record.setFocusPainted(false);
		record.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				StartupColortap.globalMouseInput.isCapturingInput = !StartupColortap.globalMouseInput.isCapturingInput;
				if (!StartupColortap.globalMouseInput.isCapturingInput) {
					record.setText("Record");
				} else {
					record.setText("Stop");
				}
			}
		});

		pollDelayLabel = new JLabel("Polling Delay (ms)");
		pollDelayLabel.setBounds(210, 30, 110, 20);
		
		pollDelay = new JTextArea("250");
		pollDelay.setBounds(210, 55, 100, 18);
		pollDelay.setBorder(border);
		
		clickDeviationLabel = new JLabel("Click Deviation");
		clickDeviationLabel.setBounds(210, 75, 100, 20);
		
		clickDeviation = new JTextArea("10");
		clickDeviation.setBounds(210, 98, 100, 18);
		clickDeviation.setBorder(border);
		
		start = new JButton("Start");
		start.setBounds(210, 121, 100, 20);
		start.setFocusPainted(false);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(thread.isRunning) {
					thread.isRunning = false;
					start.setText("Start");
				} else {
					thread = new InputRelayThread();
					new Thread(thread).start();
					start.setText("Stop");
				}
			}
		});
		
		file = new JTextArea("filename.clt");
		file.setBounds(210, 146, 100, 20);
		file.setBorder(border);
		
		save = new JButton("Save");
		save.setBounds(210, 171, 100, 20);
		save.setFocusPainted(false);
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileHandler.save(file.getText());
			}
		});
		
		load = new JButton("Load");
		load.setBounds(210, 196, 100, 20);
		load.setFocusPainted(false);
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileHandler.load(file.getText());
			}
		});
		
		add(scrollingInputList);
		add(record);
		add(pollDelayLabel);
		add(pollDelay);
		add(clickDeviationLabel);
		add(clickDeviation);
		add(start);
		add(file);
		add(save);
		add(load);
		
		setVisible(true);
		System.out.println("[WindowOverview]: Main window created");
	}

}
