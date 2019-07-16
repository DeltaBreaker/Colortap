package io.itch.deltabreaker.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.jnativehook.GlobalScreen;

import io.itch.deltabreaker.gui.WindowOverview;

public class StartupColortap {

	public static WindowOverview window;
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static MouseInputHandler globalMouseInput;

	public static void main(String[] args) {
		// Disable default console output
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		logger.setUseParentHandlers(false);

		// Create global mouse listener
		try {
			GlobalScreen.registerNativeHook();
			globalMouseInput = new MouseInputHandler();
			GlobalScreen.addNativeMouseListener(globalMouseInput);
			GlobalScreen.addNativeMouseMotionListener(globalMouseInput);
			System.out.println("[StartupColortap]: Mouse listener created");

			// Create GUI
			window = new WindowOverview();
			JOptionPane.showMessageDialog(window, "Checking Windows scaling factor.\nDon't move your mouse.");
			globalMouseInput.setScalingFactor();
			JOptionPane.showMessageDialog(window, "Scaling set to " + globalMouseInput.scalingFactor);
		} catch (Exception e) {
			System.out.println("[StartupColortap]: Failed to create mouse listener");
			e.printStackTrace();
			System.exit(0);
		}
	}

}
