package io.itch.deltabreaker.main;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import io.itch.deltabreaker.automation.ColorConditionalInput;
import io.itch.deltabreaker.gui.PanelInput;

public class MouseInputHandler implements NativeMouseInputListener {

	private Robot control;

	public boolean isCapturingInput = false;
	public double scalingFactor;

	public MouseInputHandler() {
		try {
			// Set up screen capture
			control = new Robot();
			System.out.println("[MouseInputHandler]: Created screen capture robot");
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
		// Only used to first determine the scaling factor
		if (scalingFactor == -1 && nativeEvent.getButton() == NativeMouseEvent.BUTTON3) {
			scalingFactor = Math.round(calculateScalingFactor(nativeEvent.getPoint()) * 100.0) / 100.0;
			System.out.println("[MouseInputHandler]: Scaling factor is set to " + scalingFactor);
		}
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
		// Add input to list when capturing (middle click)
		if (nativeEvent.getButton() == NativeMouseEvent.BUTTON3) {
			if (isCapturingInput) {
				try {
					// Take a screenshot
					BufferedImage screen = control.createScreenCapture(
							new Rectangle(0, 0, (int) (StartupColortap.screenSize.getWidth() * scalingFactor),
									(int) (StartupColortap.screenSize.getHeight() * scalingFactor)));

					// Get color values
					int color = screen.getRGB(nativeEvent.getX(), nativeEvent.getY());
					int r = (color >> 16) & 0xFF;
					int g = (color >> 8) & 0xFF;
					int b = color & 0xFF;

					// Add input to list with color at mouse location
					StartupColortap.window.inputList.addInput(new PanelInput(StartupColortap.window.inputList,
							new ColorConditionalInput(new Color(r, g, b),
									new Point((int) (nativeEvent.getX()), (int) (nativeEvent.getY())))));
					System.out.println("[MouseInputHandler]: Input added at " + nativeEvent.getX() + "x "
							+ nativeEvent.getY() + "y with color " + r + " " + g + " " + b);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(StartupColortap.window,
							"The mouse might be outside of the main screen");
				}
			}
		}

		// Hot key to stop play-back thread (right click)
		if (nativeEvent.getButton() == NativeMouseEvent.BUTTON2) {
			if (StartupColortap.window.thread.isRunning) {
				StartupColortap.window.thread.isRunning = false;
				StartupColortap.window.start.setText("Start");
				JOptionPane.showMessageDialog(StartupColortap.window, "Input playback has ended.");
				System.out.println("[MouseInputHandler]: Input thread stopped");
			}
		}
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent nativeEvent) {

	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent nativeEvent) {

	}

	public void setScalingFactor() {
		// Sets up calculating the scaling factor to adjust inputs
		scalingFactor = -1;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		control.mouseMove((int) screen.getWidth() / 2, (int) screen.getHeight() / 2);
		control.mousePress(InputEvent.BUTTON2_DOWN_MASK);
		control.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
	}

	private double calculateScalingFactor(Point point) {
		// Returns scaling factor based off of the fact that Robot and Toolkit respond to
		// coordinates differently than the global input handler does
		
		// If we tell the Robot to click at a specific point and then cross reference
		// that point with what the global input handler returns, we can determine what
		// the scaling is set to
		double x = point.getX() / (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2);
		double y = point.getY() / (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
		return (x + y) / 2.0;
	}

}
