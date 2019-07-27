package io.itch.deltabreaker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import io.itch.deltabreaker.automation.ColorConditionalInput;
import io.itch.deltabreaker.main.StartupColortap;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.RemoteFile;

public class WindowADBPreview extends JFrame {

	private static final long serialVersionUID = -8969474621933844578L;

	public static boolean isCapturingInput = false;
	public static BufferedImage lastScreen;
	public static Dimension size;

	public static final String title = "Colortap";
	public static JadbConnection connection;
	public static JadbDevice device;
	public static WindowADBPreview instance;

	public static boolean running = false;

	public static long a = 0L;
	public static long b = 0L;

	@SuppressWarnings("deprecation")
	public WindowADBPreview() {
		// Set up panel
		instance = this;
		setTitle(title);
		setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 / 9 * 16));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		addMouseListener(new MouseHandler());
		addMouseMotionListener(new MouseHandler());

		JPanel panel = new JPanel() {
			private static final long serialVersionUID = -7588392478902682213L;

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				try {
					g.drawImage(lastScreen, 0, 0, getWidth(), getHeight(), null);
				} catch (Exception e) {
					System.out.println(
							"[WindowADBPreview]: Error with ADB connection or ADB connection hasn't been made");
				}
			}
		};
		setUndecorated(true);
		add(panel);
		pack();
		size = getContentPane().getSize();

		setVisible(true);

		try {
			// Establish adb connection
			connection = new JadbConnection();
			device = connection.getDevices().get(0);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		while (isVisible()) {
			try {
				// Take screenshot and pull into an image object
				device.executeShell(System.out, "screencap", "-p", "/sdcard/screencap.dump");
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				device.pull(new RemoteFile("/sdcard/screencap.dump"), output);
				byte[] bytes = output.toByteArray();
				InputStream inputStream = new ByteArrayInputStream(bytes);
				lastScreen = ImageIO.read(inputStream);
				repaint();
				
				// Play back inputs
				if (running) {
					for (PanelInput i : StartupColortap.window.inputList.getInputList()) {
						int color = lastScreen.getRGB((int) i.input.getX(), (int) i.input.getY());
						int r = (color >> 16) & 0xFF;
						int g = (color >> 8) & 0xFF;
						int b = color & 0xFF;

						Color colorCheck = new Color(r, g, b);
						if (colorMatches(colorCheck, i.input.getColor(), i.getSensitivity())) {
							device.executeShell(System.out, "input", "tap", "" + i.input.getX(), "" + i.input.getY());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
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

class MouseHandler implements MouseInputListener {

	private static Point previous = new Point(0, 0);

	@Override
	public void mouseClicked(MouseEvent arg0) {
		WindowADBPreview.instance.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@SuppressWarnings("deprecation")
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			double adbScaleX = WindowADBPreview.lastScreen.getWidth() / WindowADBPreview.size.getWidth();
			double adbScaleY = WindowADBPreview.lastScreen.getHeight() / WindowADBPreview.size.getHeight();

			try {
				WindowADBPreview.device.executeShell(System.out, "input", "tap", "" + (int) (arg0.getX() * adbScaleX),
						"" + (int) (arg0.getY() * adbScaleY));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (arg0.getButton() == MouseEvent.BUTTON2) {
			if (WindowADBPreview.isCapturingInput) {
				try {
					// Get color values
					double adbScaleX = WindowADBPreview.lastScreen.getWidth() / WindowADBPreview.size.getWidth();
					double adbScaleY = WindowADBPreview.lastScreen.getHeight() / WindowADBPreview.size.getHeight();
					int color = WindowADBPreview.lastScreen.getRGB((int) (arg0.getX() * adbScaleX),
							(int) (arg0.getY() * adbScaleY));
					int r = (color >> 16) & 0xFF;
					int g = (color >> 8) & 0xFF;
					int b = color & 0xFF;

					// Add input to list with color at mouse location
					StartupColortap.window.inputList.addInput(new PanelInput(StartupColortap.window.inputList,
							new ColorConditionalInput(new Color(r, g, b),
									new Point((int) (arg0.getX() * adbScaleX), (int) (arg0.getY() * adbScaleY)))));
					System.out.println("[MouseInputHandler]: Input added at " + (int) (arg0.getX() * adbScaleX) + "x "
							+ (int) (arg0.getY() * adbScaleY) + "y with color " + r + " " + g + " " + b);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(StartupColortap.window,
							"The mouse might be outside of the main screen");
				}
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		WindowADBPreview.instance
				.setLocation(new Point((int) (WindowADBPreview.instance.getX() + (arg0.getX() - previous.getX())),
						(int) (WindowADBPreview.instance.getY() + (arg0.getY() - previous.getY()))));
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		previous = arg0.getPoint();
	}

}