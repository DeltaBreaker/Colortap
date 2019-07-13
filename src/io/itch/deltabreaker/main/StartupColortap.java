package io.itch.deltabreaker.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		window = new WindowOverview();
	}
	
}
