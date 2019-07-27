package io.itch.deltabreaker.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.jnativehook.GlobalScreen;

import io.itch.deltabreaker.gui.WindowADBPreview;
import io.itch.deltabreaker.gui.WindowOverview;

public class StartupColortap {

	public static WindowOverview window;
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static MouseInputHandler globalMouseInput;
	public static int mode;

	public static void main(String[] args) {
		// Choose which mode to start the program with
		mode = JOptionPane.showOptionDialog(null, "Start in Desktop or ADB mode?", "Message",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Desktop", "ADB" }, 0);
		if (mode == 0) {
			startDesktop();
		} else if (mode == 1) {
			startADB();
		}
	}

	public static void startDesktop() {
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

			// Determine Windows scaling factor
			globalMouseInput.setScalingFactor();
			JOptionPane.showMessageDialog(window, "Scaling set to " + globalMouseInput.scalingFactor);
		} catch (Exception e) {
			System.out.println("[StartupColortap]: Failed to create mouse listener");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void startADB() {
		try {
			// Attempt to start adb server
			System.out.println("[StartupColortap]: Attempting to start ADB Server");
			Runtime.getRuntime()
					.exec("C:\\Users\\willc\\AppData\\Local\\Android\\Sdk\\platform-tools\\adb.exe start-server");
		} catch (IOException e) {
			e.printStackTrace();
		}
		window = new WindowOverview();
		new WindowADBPreview();
	}

}
