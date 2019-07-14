package io.itch.deltabreaker.automation;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import io.itch.deltabreaker.gui.PanelInput;
import io.itch.deltabreaker.main.StartupColortap;

public class InputRelayThread implements Runnable {

	private Robot control;
	private long delay;
	private PanelInput[] inputs;
	public boolean isRunning = true;

	@Override
	public void run() {
		try {
			// Set thread variables so that no change can be made after starting
			control = new Robot();
			delay = Long.parseLong(StartupColortap.window.pollDelay.getText());
			inputs = StartupColortap.window.inputList.getInputList();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(StartupColortap.window, "Error trying to create thread!");
			isRunning = false;
			StartupColortap.window.start.setText("Start");
		}
		
		while(isRunning) {
			try {
				// Screen capture
				BufferedImage screen = control.createScreenCapture(new Rectangle(0, 0, (int) StartupColortap.screenSize.getWidth(), (int) StartupColortap.screenSize.getHeight()));
				
				// Cycle through inputs and detect any matches
				for(PanelInput i : inputs) {
					int color = screen.getRGB(i.input.getX(), i.input.getY());
					int r = (color >> 16) & 0xFF;
					int g = (color >> 8) & 0xFF;
					int b = color & 0xFF;
					
					Color colorCheck = new Color(r, g, b);
					if(colorMatches(colorCheck, i.input.getColor(), i.getSensitivity())) {
						control.mouseMove(i.input.getX(), i.input.getY());
						control.mousePress(InputEvent.BUTTON1_DOWN_MASK);
						control.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					}
				}
				
				Thread.sleep(delay);
			} catch (Exception e) {
				System.out.println("[InputRelayThread]: Error, retrying");
			}
		}
	}

	private boolean colorMatches(Color c1, Color c2, int sensitivity) {
		if (Math.abs(c1.getRed() - c2.getRed()) + Math.abs(c1.getGreen() - c2.getGreen())
				+ Math.abs(c1.getBlue() - c2.getBlue()) <= sensitivity) {
			return true;
		}
		return false;
	}

}
