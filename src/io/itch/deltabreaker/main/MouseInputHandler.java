package io.itch.deltabreaker.main;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import io.itch.deltabreaker.automation.ColorConditionalInput;
import io.itch.deltabreaker.gui.PanelInput;

public class MouseInputHandler implements NativeMouseInputListener {

	private Robot colorCapture;

	public boolean isCapturingInput = false;

	public MouseInputHandler() {
		try {
			// Set up screen capture
			colorCapture = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent nativeEvent) {

	}

	@Override
	public void nativeMousePressed(NativeMouseEvent nativeEvent) {

	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
		// Add input to list when capturing (middle click)
		if (nativeEvent.getButton() == NativeMouseEvent.BUTTON3) {
			if (isCapturingInput) {
				try {
					// Take a screenshot
					BufferedImage screen = colorCapture.createScreenCapture(new Rectangle(0, 0,
							(int) StartupColortap.screenSize.getWidth(), (int) StartupColortap.screenSize.getHeight()));
				    
					// Get color values
					int color = screen.getRGB(nativeEvent.getX(), nativeEvent.getY());
					int r = (color >> 16) & 0xFF;
					int g = (color >> 8) & 0xFF;
					int b = color & 0xFF;
					
					// Add input to list with color at mouse location
					StartupColortap.window.inputList.addInput(new PanelInput(StartupColortap.window.inputList,
							new ColorConditionalInput(new Color(r, g, b), nativeEvent.getPoint())));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(StartupColortap.window, "The mouse may be outside of the main screen"
							+ "\nor the windows scaling factor may not be set to 100%!");
				}
			}
		}
		
		// Hot key to stop play-back thread (right click)
		if (nativeEvent.getButton() == NativeMouseEvent.BUTTON2) {
			if(StartupColortap.window.thread.isRunning) {
				StartupColortap.window.thread.isRunning = false;
				StartupColortap.window.start.setText("Start");
				JOptionPane.showMessageDialog(StartupColortap.window, "Input playback has ended.");
			}
		}
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent nativeEvent) {

	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent nativeEvent) {

	}

}
