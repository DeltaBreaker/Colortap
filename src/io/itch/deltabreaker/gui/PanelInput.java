package io.itch.deltabreaker.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import io.itch.deltabreaker.automation.ColorConditionalInput;
import io.itch.deltabreaker.main.StartupColortap;

public class PanelInput extends JPanel {

	private static final long serialVersionUID = 1381980011065164091L;
	private static final Border border = BorderFactory.createLineBorder(WindowOverview.accent);
	private static final int width = 175;
	private static final int height = 82;

	public ColorConditionalInput input;
	
	private JLabel sensitivityLabel;
	private JTextArea sensitivityInput;
	private JButton remove;

	// Used to know which item to remove from parent list
	private final PanelInput self_identifier = this;

	public PanelInput(PanelInputList parentList, ColorConditionalInput input) {
		this.input = input;
		setLayout(null);
		setBorder(border);
		setSize(width, height);

		// Component setup
		remove = new JButton("Remove");
		remove.setBounds(71, 4, 100, 20);
		remove.setFocusPainted(false);
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!StartupColortap.window.thread.isRunning) {
					parentList.removeInput(self_identifier);
				}
			}
		});

		sensitivityLabel = new JLabel("Color Sensitivity");
		sensitivityLabel.setBounds(4, 40, 100, 20);

		sensitivityInput = new JTextArea("25");
		sensitivityInput.setBounds(4, 60, 50, 18);
		sensitivityInput.setBorder(border);

		add(remove);
		add(sensitivityLabel);
		add(sensitivityInput);
	}

	@Override
	public void paintComponent(Graphics g) {
		// Override paint method for custom display
		super.paintComponent(g);
		g.clearRect(0, 0, getWidth(), getHeight());

		g.setColor(WindowOverview.theme);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(WindowOverview.accent);
		g.drawString("X: " + input.getX(), 4, 13);
		g.drawString("Y: " + input.getY(), 4, 25);
		g.drawString("R: " + input.getRed() + " G: " + input.getGreen() + " B: " + input.getBlue(), 4, 37);
	}

	public int getSensitivity() {
		try {
			return Integer.parseInt(sensitivityInput.getText());
		} catch (Exception e) {
			return 0;
		}
	}

}
