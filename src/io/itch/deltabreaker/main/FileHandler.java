package io.itch.deltabreaker.main;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import io.itch.deltabreaker.automation.ColorConditionalInput;
import io.itch.deltabreaker.gui.PanelInput;

public class FileHandler {

	public static void load(String file) {
		File load = new File("data/" + file);
		if (load.exists()) {
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(load));

				StartupColortap.window.inputList.clearInputList();
				
				int length = in.readInt();
				for (int i = 0; i < length; i++) {
					ColorConditionalInput input = new ColorConditionalInput(
							new Color(in.readInt(), in.readInt(), in.readInt()), new Point(in.readInt(), in.readInt()));
					StartupColortap.window.inputList.addInput(new PanelInput(StartupColortap.window.inputList, input));
				}

				in.close();
				JOptionPane.showMessageDialog(StartupColortap.window, file + " was loaded successfully.");
				System.out.println("[FileHandler]: File " + file + " loaded");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(StartupColortap.window, "That file does not exist!");
			System.out.println("[FileHandler]: File " + file + " not found");
		}
	}

	public static void save(String file) {
		File dir = new File("data");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File save = new File(dir + "/" + file);
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(save));

			out.writeInt(StartupColortap.window.inputList.getInputList().length);
			for (PanelInput i : StartupColortap.window.inputList.getInputList()) {
				out.writeInt(i.input.getRed());
				out.writeInt(i.input.getGreen());
				out.writeInt(i.input.getBlue());
				out.writeInt(i.input.getX());
				out.writeInt(i.input.getY());
			}

			out.flush();
			out.close();
			JOptionPane.showMessageDialog(StartupColortap.window, file + " was saved successfully.");
			System.out.println("[FileHandler]: File " + file + " saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
